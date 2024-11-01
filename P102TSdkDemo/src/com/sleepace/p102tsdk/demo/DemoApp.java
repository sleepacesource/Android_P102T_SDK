package com.sleepace.p102tsdk.demo;

import com.sleepace.p102tsdk.demo.util.CrashHandler;
import com.sleepace.sdk.manager.DeviceType;
import com.sleepace.sdk.p102t.domain.Analysis;
import com.sleepace.sdk.p102t.domain.Detail;
import com.sleepace.sdk.p102t.domain.Summary;
import com.sleepace.sdk.p102t.util.AnalysisUtil;
import com.sleepace.sdk.util.SdkLog;

import android.app.Application;


public class DemoApp extends Application {

    private static DemoApp instance;
    
    public StringBuffer logBuf = new StringBuffer();

    public static DemoApp getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        CrashHandler.getInstance().init(this);
        SdkLog.init(this);
        SdkLog.setLogEnable(true);
        String logDir = getExternalFilesDir("log").getPath();
        SdkLog.setLogDir(logDir);
        SdkLog.setSaveLog(true);
        
//        Summary summ = new Summary();
//		summ.setStartTime(1729637313);
//		summ.setRecordCount(283);
//		summ.setArithmeticVer("02.00.01");
//		
//		Detail detail = new Detail();
//		detail.setData1(new int[] {239411972, 239280897, 340402945, 374153985, 373564161, 357245697, 340468481, 306848259, 374014208, 373817600, 357105920, 356974848, 340197632, 356974848, 356983553, 289276160, 322961664, 
//				322437376, 339280128, 339345664, 339345664, 322437376, 339542272, 339607808, 339083520, 338952448, 339411200, 339542272, 339149056, 339280128, 339280128, 339083520, 339411200, 339411200, 
//				339804416, 322896128, 339149056, 339214592, 339476736, 322765056, 322699520, 322896128, 322699520, 339738880, 322896128, 322765056, 339419905, 356319488, 339542272, 339804416, 322970113, 
//				339673344, 339673344, 339607808, 289865984, 306708736, 340066560, 306512128, 323223808, 289940225, 339607808, 323223808, 306446592, 306446592, 356778240, 289669376, 339935488, 306315520, 
//				323289344, 306381056, 323223808, 323354880, 323289344, 339616513, 305725696, 322896128, 322896128, 323092736, 322699520, 322830592, 322765056, 322830592, 322502912, 323092736, 323027200, 
//				322961664, 323027200, 322765056, 322961664, 322699520, 322765056, 322830592, 323027200, 306118912, 322896128, 306053376, 322896128, 322830592, 322765056, 322961664, 272311041, 322773761, 
//				339345664, 338952448, 356188416, 339485441, 322568448, 322642689, 323158272, 322773505, 306118912, 289079552, 255533825, 322502912, 339149056, 322306304, 339083520, 322371840, 339083520, 
//				356000513, 305931009, 305725696, 305856768, 305922304, 306053376, 306053376, 306118912, 322830592, 322830592, 322896128, 306118912, 305922304, 306118912, 305922304, 306118912, 306118912, 
//				305987840, 306053376, 323027200, 306053376, 306053376, 306184448, 306184448, 306053376, 306118912, 306184448, 306118912, 305987840, 306193154, 339878659, 389686018, 373170945, 322830592, 
//				356450560, 356516096, 373096704, 339157762, 389751555, 339419907, 373236482, 355476225, 339288834, 339223297, 339223297, 339550978, 339485186, 356590337, 356393730, 356066051, 373498626, 
//				339223299, 339288834, 272245505, 272179719, 255468289, 339485185, 339682050, 339288835, 339157761, 322904833, 339682049, 339944194, 238953217, 222438146, 322437376, 372908545, 356253952, 
//				356253952, 356393473, 339542272, 356253952, 356262401, 373170945, 339354369, 373039873, 356131330, 356393730, 356328194, 288817408, 255459584, 255402497, 288882944, 305660160, 305660160, 
//				305725696, 356385024, 339345664, 322502912, 305725696, 322502912, 305791232, 305734401, 356459265, 255459584, 238756610, 272433408, 255459584, 356590337, 322765056, 306127617, 305799937, 
//				289341696, 339411200, 272433408, 305922304, 322765056, 339354369, 289219073, 306127617, 289276160, 322765056, 306184448, 306446592, 306118912, 323027200, 306249984, 306062081, 272433408, 
//				272704001, 289407232, 322830592, 305987840, 323035905, 305472001, 322437376, 305791232, 305799681, 305856768, 322699520, 305922304, 306053376, 306053376, 305865217, 305987840, 289145088, 
//				306053376, 272302336, 305922304, 305987840, 305987840, 322765056, 305987840, 306053376, 305987840, 305987840, 305987840, 306053376, 305987840, 305987840, 305987840, 306053376, 289276160, 
//				306062081, 339149056, 322371840, 322502912, 305660160, 305660160, 322568448, 305660160, 322568448, 305734402, 239084289});
//		detail.setData2(new int[] {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 
//				-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 
//				-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
//				-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
//				-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1});
//		detail.setData3(new int[] {0, 0, 0, 0, 0, 0, 0, 0, 0, 4, 3, 4, 4, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 8, 9, 10, 11, 11, 11, 15, 16, 17, 18, 20, 21, 22, 24, 25, 26, 27, 28, 28, 30, 29, 28, 28, 28, 28, 27, 27, 27, 27, 25, 24, 21, 21, 21, 20, 20, 20, 20, 20, 
//				19, 19, 18, 17, 17, 17, 13, 15, 14, 14, 13, 13, 13, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 13, 14, 15, 16, 17, 18, 20, 21, 22, 23, 24, 25, 25, 25, 25, 25, 25, 24, 23, 22, 21, 20, 19, 18, 18, 17, 16, 15, 14, 13, 12, 11, 10, 11, 
//				11, 11, 11, 11, 11, 11, 11, 11, 12, 13, 14, 14, 15, 16, 17, 18, 19, 19, 19, 20, 21, 22, 23, 24, 25, 26, 27, 27, 27, 27, 26, 25, 24, 22, 22, 21, 20, 19, 18, 17, 16, 15, 14, 13, 12, 11, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1, 1, 1, 1, 1, 1, 1, 
//				1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 3, 4, 4, 4, 5, 5, 5, 5, 5, 5, 6, 6, 6, 6, 6, 6, 6, 8, 9, 10, 11, 11, 11, 11, 11, 11, 10, 10, 9, 9, 15, 14, 15, 10, 7, 7, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 
//				10, 10, 9, 9, 10, 10, 11, 12, 13, 16, 17, 18, 19, 20, 21, 22, 23, 23, 23, 23, 25, 25, 25, 25, 25, 25, 24, 24});
//		detail.setData4(new int[] {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 
//				-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 
//				-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
//				-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
//				-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1});
//		
//		int[] data3 = new int[detail.getData3().length];
//		for(int i=0; i<data3.length;i++) {
//			data3[i] = ((detail.getData3()[i] << 24) | 0xFFFFFF);
//		}
//		detail.setData3(data3);
//		
//		Analysis analysis = AnalysisUtil.analysData(summ, detail, 1);
//		SdkLog.log("analysis:" + analysis);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    @Override
    public void onLowMemory() {
        // 低内存的时候执行
        super.onLowMemory();
    }

    @Override
    public void onTrimMemory(int level) {
        // 程序在内存清理的时候执行
        super.onTrimMemory(level);

    }

}














