# GlideLifecycleSample

根据Glide使用空Fragment监听Activity生命周期的原理，配合策略模式，对项目中的身份证识别进行重构

## 策略模式优化各种扫身份证
使用策略模式，抽象各种扫身份证实现的IIDCardScan接口
扫身份证成功失败的回调接口IDCardScanResultCallback
```
/**
 * 对各种身份证扫码的最高层抽象
 */
public interface IIDCardScan {

    void startIDCardScan(IDCardScanResultCallback idCardScanResultCallback);

    void stopIDCardScan();

    void destoryIDCardScan();
}
```

```
/**
 * 扫身份证成功失败返回结果的回调接口，给最上调用层的回调
 */
public interface IDCardScanResultCallback {

    void onIDCardScanSuccess(IDCardInfoBean idCardInfoBean);

    void onIDCardError(String resCode, String resMsg);

}
```

IDCardScanEngine
```
public class IDCardScanEngine {

    Context mContext;
    IIDCardScan mIIDCardScan;
    IDCardScanResultCallback mIdCardScanResultCallback;

    private static volatile IDCardScanEngine instance;

    public static IDCardScanEngine getInstance(Context context) {
        if (instance == null) {
            synchronized (IDCardScanEngine.class) {
                if (instance == null) {
                    instance = new IDCardScanEngine(context);
                }
            }
        }
        return instance;
    }

    public IDCardScanEngine(Context context) {
        this.mContext = context;

        mIIDCardScan = getIDCardScanEngine(context);
    }

    /**
     * 通过策略模式
     * 根据机型，初始化不同的身份证读取实现
     *
     * @param context
     * @return
     */
    public IIDCardScan getIDCardScanEngine(Context context) {

        // 仅示例演示
        return SampleIDCardScan.getInstance(context);

        // 实际应根据model判断机型
        /*if(...){
            return ...;
        }
        return null;*/

    }

    public void startScanIDCard(IDCardScanResultCallback idCardScanResultCallback){

        this.mIdCardScanResultCallback = idCardScanResultCallback;

        mIIDCardScan.startIDCardScan(idCardScanResultCallback);
    }

    public void pauseScanIDCard(){
        mIIDCardScan.pauseScanIDCard();
    }

    public void stopScanIDCard(){
        mIIDCardScan.stopIDCardScan();
    }

    public void destoryIDCardScan(){
        mIIDCardScan.destoryIDCardScan();
        instance = null;
    }
}
```

调用层
```
    iDCardScanEngine = IDCardScanEngine.getInstance(this.getApplicationContext());
    
    IDCardScanEngine.startScanIDCard(new IDCardScanResultCallback(){
        @Override
        public void onIDCardScanSuccess(IDCardInfoBean idCardInfoBean) {
            // do something
        }
        @Override
        public void onIDCardError(String resCode, String resMsg) {
        }
    });
```
通过iDCardScanEngine对象，在各生命周期中做对应的停止、销毁处理
```
    @Override
    protected void onStop() {
        super.onStop();
        if(iDCardScanEngine != null){
            iDCardScanEngine.stopScanIDCard();
        }
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(iDCardScanEngine != null){
            iDCardScanEngine.destoryIDCardScan();
        }
    }    
```

万一忘记了手动调用，就会引起耗电量加剧等问题

## 仿Glide思想写一个绑定生命周期的身份证识别
先定义相关的生命周期接口，主要是暂停和销毁。            
onPause可见不可交互（弹出弹框等），onStop不可见不可交互
```
public interface LifeCycleListener {

    void onPause();

    void onStop();

    void onDestroy();

}
```
然后，利用Fragment感知Activity生命周期的特性，在对应的生命周期方法中调用对应的生命周期接口方法
```
public class SupportManagerFragment extends Fragment {

    private LifeCycleListener lifeCycle;

    public SupportManagerFragment(@NonNull LifeCycleListener lifeCycle) {
        this.lifeCycle = lifeCycle;
    }

    public void addListener(@NonNull LifeCycleListener listener){
        this.lifeCycle = listener;
    }

    LifeCycleListener getLifeCycle(){
        return lifeCycle;
    }


    @Override
    public void onPause() {
        super.onPause();
        if(lifeCycle!=null){
            lifeCycle.onPause();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if(lifeCycle!=null){
            lifeCycle.onStop();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(lifeCycle!=null){
            lifeCycle.onDestroy();
        }
    }

}
```
定义一个类IDCardScanManager，实现了生命周期接口，将自身传入SupportManagerFragment。
然后将SupportManagerFragment与传入的FragmnetAcitivty绑定。
这样，当FragmnetAcitivty生命周期发生改变时，SupportManagerFragment能感知到，然后通过生命周期接口，让该接口的实现类在对应生命周期中做对应的停止、销毁处理。就不用在Activity中手动调用处理方法了。           
```
public class IDCardScanManager implements LifeCycleListener {


    Context mContext;

    IDCardScanEngine mIDCardScanEngine;

    private final String FRAGMENT_ACTIVITY_NAME = "FRAGMENT_ACTIVITY_NAME";


    public void with(Context context){
        if (context == null) {
            throw new IllegalArgumentException("You cannot start a load on a null Context");
        }
        this.mContext = context;

        createIDCardScanEngine(context);
        bind(context);

    }

    private void createIDCardScanEngine(Context context) {
        mIDCardScanEngine = IDCardScanEngine.getInstance(context);
    }

    /**
     * @param context 传入的上下文
     */
    public void bind(Context context) {
        if (Util.isOnMainThread() && !(context instanceof Application)) {
            if (context instanceof FragmentActivity) {
                bindFragmentActivity((FragmentActivity) context);
            }
        }
    }

    private void bindFragmentActivity(FragmentActivity activity) {
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        supportFragmentGet(fragmentManager);
    }

    /**
     * 创建Fragment 并依托于当前Activity(当然Glide做了缓存去重操作)
     * @param fragmentManager
     */
    private void supportFragmentGet(FragmentManager fragmentManager) {

        Fragment fragment = fragmentManager.findFragmentByTag(FRAGMENT_ACTIVITY_NAME);
        if (fragment == null) {
            SupportManagerFragment current = new SupportManagerFragment(this);

            fragmentManager.beginTransaction()
                    .add(current, FRAGMENT_ACTIVITY_NAME)
                    .commitAllowingStateLoss();

        }
    }

    public void startScanIDCard(IDCardScanResultCallback idCardScanResultCallback){
        mIDCardScanEngine.startScanIDCard(idCardScanResultCallback);
    }


    @Override
    public void onPause() {
        mIDCardScanEngine.pauseScanIDCard();
    }

    @Override
    public void onStop() {
        mIDCardScanEngine.stopScanIDCard();
    }

    @Override
    public void onDestroy() {
        mIDCardScanEngine.destoryIDCardScan();
    }


}
```
将主要的初始化方法合并到with方法，供外界调用                   

定义一个供外界调用的类IDCardScan，通过外观模式，屏蔽实现细节
```
public class IDCardScan {

    public static IDCardScanManager with(FragmentActivity fragmentActivity){
        IDCardScanManager iDCardScanManager = new IDCardScanManager();
        iDCardScanManager.with(fragmentActivity);
        return iDCardScanManager;
    }
}
```

调用层
```
    IDCardScan.with(this).startScanIDCard(new IDCardScanResultCallback(){
        @Override
        public void onIDCardScanSuccess(IDCardInfoBean idCardInfoBean) {
            // do something
        }
        @Override
        public void onIDCardError(String resCode, String resMsg) {
        }
    });
```

## 监听生命周期的实际效果
![image](https://github.com/CoderWalterXu/GlideLifecycleSample/blob/master/screenshot/sample.jpg)
