package com.example.vinicius.vovoseguro;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.zhaoxiaodan.miband.ActionCallback;
import com.zhaoxiaodan.miband.MiBand;
import com.zhaoxiaodan.miband.listeners.HeartRateNotifyListener;
import com.zhaoxiaodan.miband.listeners.NotifyListener;
import com.zhaoxiaodan.miband.listeners.RealtimeStepsNotifyListener;
import com.zhaoxiaodan.miband.model.BatteryInfo;
import com.zhaoxiaodan.miband.model.LedColor;
import com.zhaoxiaodan.miband.model.UserInfo;
import com.zhaoxiaodan.miband.model.VibrationMode;

import java.util.Arrays;

public class Test extends AppCompatActivity {

    MiBand miband = new MiBand(this);

    BluetoothDevice device;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        final ScanCallback scanCallback = new ScanCallback()
        {
            @Override
            public void onScanResult(int callbackType, ScanResult result)
            {
                BluetoothDevice device = result.getDevice();
                Log.d("",
                        "dispositivos proximos: name:" + device.getName() + ",uuid:"
                + device.getUuids() + ",add:"
                + device.getAddress() + ",type:"
                + device.getType() + ",bondState:"
                + device.getBondState() + ",rssi:" + result.getRssi());
                // 根据情况展
            }
        };
        MiBand.startScan(scanCallback);

// 停止扫描
        MiBand.stopScan(scanCallback);

// 连接, 指定刚才扫描到的设备中的一个
//        miband.connect(device, new ActionCallback() {
//
//            @Override
//            public void onSuccess(Object data)
//            {
//                Log.d("","connect success");
//            }
//
//            @Override
//            public void onFail(int errorCode, String msg)
//            {
//                Log.d("","connect fail, code:"+errorCode+",mgs:"+msg);
//            }
//        });

// 设置断开监听器, 方便在设备断开的时候进行重连或者别的处理
        miband.setDisconnectedListener(new NotifyListener()
        {
            @Override
            public void onNotify(byte[] data)
            {
                Log.d("", "desconectado!!!");
            }
        });

// 设置UserInfo, 心跳检测之前必须设置
// 当最后一个参数Type 为 1, 每次手环都会闪烁并震动, 这个时候, 需要拍一下手环, 以确认配对; 就像官方app 配对时一样
// 当 Type为0, 只有当设置的 userid 跟之前设置的不一样时, 才需要确认配对;
// 当 Type为0, 且 设置的 userid 跟之前一样时 手环无反应; 会在normal notify 收到一个值为3的通知

        UserInfo userInfo = new UserInfo(20111111, 1, 32, 180, 55, "胖梁", 0);
        miband.setUserInfo(userInfo);

// 设置心跳扫描结果通知
        miband.setHeartRateScanListener(new HeartRateNotifyListener()
        {
            @Override
            public void onNotify(int heartRate)
            {
                Log.d("batimento", "heart rate: "+ heartRate);
            }
        });

//开始心跳扫描
        miband.startHeartRateScan();

// 读取和连接设备的信号强度Rssi值
        miband.readRssi(new ActionCallback() {

            @Override
            public void onSuccess(Object data)
            {
                Log.d("", "rssi:"+(int)data);
            }

            @Override
            public void onFail(int errorCode, String msg)
            {
                Log.d("", "readRssi fail");
            }
        });

// 读取手环电池信息
        miband.getBatteryInfo(new ActionCallback() {

            @Override
            public void onSuccess(Object data)
            {
                BatteryInfo info = (BatteryInfo)data;
                Log.d("", info.toString());
                //cycles:4,level:44,status:unknow,last:2015-04-15 03:37:55
            }

            @Override
            public void onFail(int errorCode, String msg)
            {
                Log.d("", "readRssi fail");
            }
        });

//震动2次， 三颗led亮
        miband.startVibration(VibrationMode.VIBRATION_WITH_LED);

//震动2次, 没有led亮
        miband.startVibration(VibrationMode.VIBRATION_WITHOUT_LED);

//震动10次, 中间led亮蓝色
        miband.startVibration(VibrationMode.VIBRATION_10_TIMES_WITH_LED);

//停止震动, 震动时随时调用都可以停止
        miband.stopVibration();

//获取普通通知, data一般len=1, 值为通知类型, 类型暂未收集
        miband.setNormalNotifyListener(new NotifyListener() {

            @Override
            public void onNotify(byte[] data)
            {
                Log.d("", "NormalNotifyListener:" + Arrays.toString(data));
            }
        });

// 获取实时步数通知, 设置好后, 摇晃手环(需要持续摇动10-20下才会触发), 会实时收到当天总步数通知
// 使用分两步:
// 1.设置监听器

        miband.setRealtimeStepsNotifyListener(new RealtimeStepsNotifyListener() {

            @Override
            public void onNotify(int steps)
            {
                Log.d("", "RealtimeStepsNotifyListener:" + steps);
            }
        });

// 2.开启通知
        miband.enableRealtimeStepsNotify();

//关闭(暂停)实时步数通知, 再次开启只需要再次调用miband.enableRealtimeStepsNotify()即可
        miband.disableRealtimeStepsNotify();

//设置led颜色, 橙, 蓝, 红, 绿
        miband.setLedColor(LedColor.ORANGE);
        miband.setLedColor(LedColor.BLUE);
        miband.setLedColor(LedColor.RED);
        miband.setLedColor(LedColor.GREEN);

// 获取重力感应器原始数据, 需要两步
// 1. 设置监听器
        miband.setSensorDataNotifyListener(new NotifyListener()
        {
            @Override
            public void onNotify(byte[] data)
            {
                int i = 0;

                int index = (data[i++] & 0xFF) | (data[i++] & 0xFF) << 8;  // 序号
                int d1 = (data[i++] & 0xFF) | (data[i++] & 0xFF) << 8;
                int d2 = (data[i++] & 0xFF) | (data[i++] & 0xFF) << 8;
                int d3 = (data[i++] & 0xFF) | (data[i++] & 0xFF) << 8;

            }
        });

// 2. 开启
        miband.enableSensorDataNotify();

// 配对, 貌似没啥用, 不配对也可以做其他的操作
        miband.pair(new ActionCallback() {
            @Override
            public void onSuccess(Object data)
            {
                changeStatus("pair succ");
            }

            @Override
            public void onFail(int errorCode, String msg)
            {
                changeStatus("pair fail");
            }
        });
    }

    private void changeStatus(String pair_fail) {
    }
}
