package com.sleepace.p102tsdk.demo.fragment;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.sleepace.p102tsdk.demo.MainActivity;
import com.sleepace.p102tsdk.demo.R;
import com.sleepace.p102tsdk.demo.RawDataActivity;
import com.sleepace.p102tsdk.demo.util.HistoryDataComparator;
import com.sleepace.sdk.constant.StatusCode;
import com.sleepace.sdk.interfs.IConnectionStateCallback;
import com.sleepace.sdk.interfs.IDeviceManager;
import com.sleepace.sdk.interfs.IResultCallback;
import com.sleepace.sdk.manager.CONNECTION_STATE;
import com.sleepace.sdk.manager.CallbackData;
import com.sleepace.sdk.manager.DeviceType;
import com.sleepace.sdk.p102t.CallbackType;
import com.sleepace.sdk.p102t.P102TDeviceManager.RealtimeDataListener;
import com.sleepace.sdk.p102t.domain.CollectStatus;
import com.sleepace.sdk.p102t.domain.Detail;
import com.sleepace.sdk.p102t.domain.HistoryData;
import com.sleepace.sdk.p102t.domain.RealTimeData;
import com.sleepace.sdk.p102t.util.SleepStatusType;
import com.sleepace.sdk.util.SdkLog;
import com.sleepace.sdk.util.StringUtil;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class DataFragment extends BaseFragment {

	private Button btnRealtimeData, btnReport;
	private TextView tvSleepStatus, tvHeartRate, tvBreathRate, tvTemp, tvHum;
	private View envirView;
	private ProgressDialog progressDialog;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View view = inflater.inflate(R.layout.fragment_control, null);
		// SdkLog.log(TAG+" onCreateView-----------");
		findView(view);
		initListener();
		initUI();
		return view;
	}

	protected void findView(View root) {
		// TODO Auto-generated method stub
		super.findView(root);
		btnRealtimeData = (Button) root.findViewById(R.id.btn_realtime_data);
		btnReport = (Button) root.findViewById(R.id.btn_create_report);

		tvSleepStatus = (TextView) root.findViewById(R.id.tv_sleep_status);
		tvHeartRate = (TextView) root.findViewById(R.id.tv_heartrate);
		tvBreathRate = (TextView) root.findViewById(R.id.tv_breathrate);
		envirView = root.findViewById(R.id.layout_envir_data);
		tvTemp = (TextView) root.findViewById(R.id.tv_temp);
		tvHum = (TextView) root.findViewById(R.id.tv_hum);
	}

	protected void initListener() {
		// TODO Auto-generated method stub
		super.initListener();
		getDeviceHelper().addConnectionStateCallback(stateCallback);
		getDeviceHelper().addRealtimeDataListener(realtimeDataListener);
		btnRealtimeData.setOnClickListener(this);
		btnReport.setOnClickListener(this);
	}

	protected void initUI() {
		// TODO Auto-generated method stub
		mActivity.setTitle(R.string.control_);
		if (mActivity.getDevice() != null) {
			if (DeviceType.isP3(mActivity.getDevice().getDeviceType())) {
				envirView.setVisibility(View.VISIBLE);
			} else {
				envirView.setVisibility(View.GONE);
			}
		} else {
			envirView.setVisibility(View.GONE);
		}
		initBtnRealtime();

		progressDialog = new ProgressDialog(mActivity);
		progressDialog.setIcon(android.R.drawable.ic_dialog_info);
		progressDialog.setMessage(getString(R.string.sleep_analysis));
		progressDialog.setCancelable(false);
		progressDialog.setCanceledOnTouchOutside(false);
	}

	private void initBtnRealtime() {
		if (MainActivity.realtimeDataOpen) {
			btnRealtimeData.setText(R.string.stop_view_data);
		} else {
			btnRealtimeData.setText(R.string.view_data);
			tvSleepStatus.setText(null);
			tvHeartRate.setText(null);
			tvBreathRate.setText(null);
			tvTemp.setText(null);
			tvHum.setText(null);
		}
	}

	private void initBtnState(boolean connect) {
		if (connect) {
			btnRealtimeData.setEnabled(true);
			btnReport.setEnabled(true);
		} else {
			btnRealtimeData.setEnabled(false);
			btnReport.setEnabled(false);
		}
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		initBtnState(getDeviceHelper().isConnected());
		SdkLog.log(TAG + " onResume realtimeDataOpen:" + MainActivity.realtimeDataOpen);
	}

	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();
		SdkLog.log(TAG + " onDestroyView----------------");
		getDeviceHelper().removeConnectionStateCallback(stateCallback);
		getDeviceHelper().removeRealtimeDataListener(realtimeDataListener);
	}

	private RealtimeDataListener realtimeDataListener = new RealtimeDataListener() {
		@Override
		public void onReceive(final RealTimeData realTimeData) {
			// TODO Auto-generated method stub
			if(!MainActivity.realtimeDataOpen) {
				MainActivity.realtimeDataOpen = true;
			}
			
			mActivity.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					if (!isFragmentVisible()) {
						return;
					}

					int sleepStatus = realTimeData.getStatus();
					int statusRes = getSleepStatus(sleepStatus);
					if (statusRes > 0) {
						tvSleepStatus.setText(statusRes);
					} else {
						tvSleepStatus.setText(null);
					}

					if (sleepStatus == SleepStatusType.SLEEP_INIT || sleepStatus == SleepStatusType.SLEEP_LEAVE
							|| (realTimeData.getHeartRate() == 255 && realTimeData.getBreathRate() == 255)) {// 离床
						tvHeartRate.setText("--");
						tvBreathRate.setText("--");
					} else {
						tvHeartRate.setText(realTimeData.getHeartRate() + getString(R.string.unit_heart));
						tvBreathRate.setText(realTimeData.getBreathRate() + getString(R.string.unit_respiration));
					}

					if (sleepStatus == SleepStatusType.SLEEP_INIT) {
						tvTemp.setText("--");
						tvHum.setText("--");
					} else {
						tvTemp.setText(realTimeData.getTemp() + "℃");
						tvHum.setText(realTimeData.getHumidity() + "%");
					}
				}
			});
		}
	};

	private IConnectionStateCallback stateCallback = new IConnectionStateCallback() {
		@Override
		public void onStateChanged(IDeviceManager manager, final CONNECTION_STATE state) {
			// TODO Auto-generated method stub
			mActivity.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub

					if (!isFragmentVisible()) {
						return;
					}

					if (state == CONNECTION_STATE.DISCONNECT) {
						initBtnState(false);
						tvSleepStatus.setText(null);
						tvHeartRate.setText(null);
						tvBreathRate.setText(null);
						tvTemp.setText(null);
						tvHum.setText(null);
					} else if (state == CONNECTION_STATE.CONNECTED) {
						initBtnState(true);
					}
				}
			});
		}
	};

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
		if (v == btnReport) {
			progressDialog.show();
			getDeviceHelper().queryCollectStatus(3000, new IResultCallback<CollectStatus>() {
				@Override
				public void onResultCallback(CallbackData<CollectStatus> cd) {
					// TODO Auto-generated method stub
					SdkLog.log(TAG + " queryCollectStatus cd:" + cd);
					if (!isFragmentVisible()) {
						return;
					}

					if (cd.isSuccess()) {
						CollectStatus collectStatus = cd.getResult();
						int curTime = (int) (System.currentTimeMillis() / 1000);
						SdkLog.log(TAG + " queryCollectStatus curTime:" + StringUtil.DATE_FORMAT.format(new Date(curTime * 1000l)) + ",collectStatus:"
								+ collectStatus);
						// 采集时间小时10分钟
						if (false && (collectStatus.getTimestamp() == 0 || curTime - collectStatus.getTimestamp() < 10 * 60)) {
							mActivity.runOnUiThread(new Runnable() {
								@Override
								public void run() {
									// TODO Auto-generated method stub
									progressDialog.dismiss();
									mActivity.showMessage("", getString(R.string.hint_analyze_fail));
								}
							});
						} else {
							// 下载历史数据前，建议停止原始数据上报，如未开启原始数据上报，则忽略此步骤
							getDeviceHelper().stopOriginalData(3000, new IResultCallback<Void>() {
								@Override
								public void onResultCallback(CallbackData<Void> cd) {
									// TODO Auto-generated method stub
									if (!isFragmentVisible()) {
										return;
									}
									if (cd.getCallbackType() == CallbackType.RAW_DATA_STOP) {
										SdkLog.log(TAG + " stopOriginalData cd:" + cd);
									}
								}
							});

							// 下载历史数据前，建议停止实时数据上报，如未开启实时数据上报，则忽略此步骤
							getDeviceHelper().stopRealTimeData(3000, new IResultCallback<Void>() {
								@Override
								public void onResultCallback(CallbackData<Void> cd) {
									// TODO Auto-generated method stub
									if (!isFragmentVisible()) {
										return;
									}
									if (cd.getCallbackType() == CallbackType.REALTIME_DATA_STOP) {
										SdkLog.log(TAG + " stopRealTimeData cd:" + cd);
									}
								}
							});

							// 下载历史数据前，建议停止原始数据上报，实时数据上报
							getDeviceHelper().stopCollection(3000, new IResultCallback<Void>() {
								@Override
								public void onResultCallback(CallbackData<Void> cd) {
									// TODO Auto-generated method stub
									if (!isFragmentVisible()) {
										return;
									}
									if (cd.getCallbackType() == CallbackType.COLLECT_STOP) {
										SdkLog.log(TAG + " stopCollection cd:" + cd);
									}

									Calendar cal = Calendar.getInstance();
									int endTime = (int) (cal.getTimeInMillis() / 1000);
									cal.add(Calendar.DATE, -30);
									cal.set(Calendar.HOUR_OF_DAY, 0);
									cal.set(Calendar.MINUTE, 0);
									cal.set(Calendar.SECOND, 0);
									int startTime = (int) (cal.getTimeInMillis() / 1000);
//									int startTime = 1729955248;
									getDeviceHelper().historyDownload(startTime, endTime, 1, new IResultCallback<List<HistoryData>>() {
										@Override
										public void onResultCallback(final CallbackData<List<HistoryData>> cd) {
											// TODO Auto-generated method stub
											if (!isFragmentVisible()) {
												return;
											}
											// 同步完报告后，重新打开实时数据
											if (MainActivity.realtimeDataOpen) {
												getDeviceHelper().startRealTimeData(1000, new IResultCallback<Void>() {
													@Override
													public void onResultCallback(CallbackData<Void> cd) {
														// TODO Auto-generated method stub
														
													}
												});
											}

											mActivity.runOnUiThread(new Runnable() {
												@Override
												public void run() {
													// TODO Auto-generated method stub
													progressDialog.dismiss();
													if (cd.isSuccess()) {
														List<HistoryData> list = cd.getResult();
														SdkLog.log(TAG + " historyDownload size:" + list.size());
														if (list.size() > 0) {
															Collections.sort(list, new HistoryDataComparator());
															HistoryData historyData = list.get(0);
															Detail detail = historyData.getDetail();
															SdkLog.log(TAG + " historyDownload status:" + Arrays.toString(detail.getStatus()));
															SdkLog.log(
																	TAG + " historyDownload statusVal:" + Arrays.toString(detail.getStatusValue()));
															SdkLog.log(TAG + " historyDownload first data duration:"
																	+ historyData.getSummary().getRecordCount() + ",algorithmVer:"
																	+ historyData.getAnalysis().getAlgorithmVer());
															mActivity.showTab(MainActivity.TAB_HISTORY_REPORT, historyData);
														} else {
															// Toast.makeText(mActivity, R.string.hint_analyze_fail, Toast.LENGTH_LONG).show();
														}
													} else {
														SdkLog.log(TAG + " historyDownload fail cd:" + cd);
														if (cd.getStatus() == StatusCode.DISCONNECT) {
															mActivity.showMessage("", getString(R.string.opt_fail));
														}
													}
												}
											});
										}
									});
								}
							});
						}
					} else {
						mActivity.runOnUiThread(new Runnable() {
							@Override
							public void run() {
								// TODO Auto-generated method stub
								progressDialog.dismiss();
								mActivity.showMessage("", getString(R.string.opt_fail));
							}
						});
					}
				}
			});
		} else if (v == btnRealtimeData) {
			SdkLog.log(TAG + " startRealtime realtimeDataOpen:" + MainActivity.realtimeDataOpen);
			printLog(R.string.view_data);
			if (MainActivity.realtimeDataOpen) {
				getDeviceHelper().stopRealTimeData(3000, new IResultCallback<Void>() {
					@Override
					public void onResultCallback(final CallbackData<Void> cd) {
						// TODO Auto-generated method stub
						SdkLog.log(TAG+" stopRealTimeData cd:" + cd +",isAdd:" + isAdded());
						if (cd.isSuccess()) {
							MainActivity.realtimeDataOpen = false;
						}

						mActivity.runOnUiThread(new Runnable() {
							@Override
							public void run() {
								// TODO Auto-generated method stub
								if (!isFragmentVisible()) {
									return;
								}

								if (cd.isSuccess()) {
									// printLog(R.string.stop_data_successfully);
									initBtnState(true);
									initBtnRealtime();
									tvSleepStatus.setText("--");
									tvHeartRate.setText("--");
									tvBreathRate.setText("--");
									tvTemp.setText("--");
									tvHum.setText("--");
								} else {
									if (cd.getStatus() == StatusCode.DISCONNECT) {
										mActivity.showMessage("", getString(R.string.opt_fail));
									}
								}
							}
						});
					}
				});
			} else {
				getDeviceHelper().startRealTimeData(3000, new IResultCallback<Void>() {
					@Override
					public void onResultCallback(final CallbackData<Void> cd) {
						// TODO Auto-generated method stub
						SdkLog.log(TAG+" startRealTimeData cd:" + cd +",isAdd:" + isAdded());
						if (cd.isSuccess()) {
							MainActivity.realtimeDataOpen = true;
						}
						mActivity.runOnUiThread(new Runnable() {
							@Override
							public void run() {
								// TODO Auto-generated method stub
								if (!isFragmentVisible()) {
									return;
								}

								if (cd.isSuccess()) {
									initBtnState(true);
									initBtnRealtime();
								} else {
									if (cd.getStatus() == StatusCode.DISCONNECT) {
										mActivity.showMessage("", getString(R.string.opt_fail));
									}
								}
							}
						});
					}
				});
			}
		} else if (v == null) {
			Intent intent = new Intent(mActivity, RawDataActivity.class);
			startActivity(intent);
		}
	}

	private int getSleepStatus(int status) {
		int statusRes = 0;
		switch (status) {
		case SleepStatusType.SLEEP_OK:
			statusRes = R.string.normal;
			break;
		case SleepStatusType.SLEEP_INIT:
			statusRes = R.string.initialization;
			break;
		case SleepStatusType.SLEEP_B_STOP:
			statusRes = R.string.respiration_pause;
			break;
		case SleepStatusType.SLEEP_H_STOP:
			statusRes = R.string.heartbeat_pause;
			break;
		case SleepStatusType.SLEEP_BODYMOVE:
			statusRes = R.string.body_movement;
			break;
		case SleepStatusType.SLEEP_LEAVE:
			statusRes = R.string.out_bed;
			break;
		case SleepStatusType.SLEEP_TURN_OVER:
			statusRes = R.string.label_turn_over;
			break;
		}

		return statusRes;
	}
}
