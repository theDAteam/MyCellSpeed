package com.mycellspeed.main.fragment;

import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ProgressEvent;
import com.amazonaws.services.s3.model.ProgressListener;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.mycellspeed.main.R;
import com.mycellspeed.main.aws.DynamoDBHandler;
import com.mycellspeed.main.singleton.DataUnitHandler;
import com.mycellspeed.main.singleton.ServiceDataHandler;
import com.mycellspeed.main.singleton.SystemDataHandler;
import com.mycellspeed.main.task.TaskResult;

import android.app.ProgressDialog;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Spinner;
import android.widget.TextView;

public class DataInfoFragment extends Fragment implements OnClickListener {
	public static final String S3_BUCKET = "com.mycellspeed.dev";
	public static final String S3_DATA_DEST = "/android_inbound";
	public static final String S3_DOWNLOAD = "/android_outbound";
	public static final String S3_UPLOAD = "/android_garbage";
	public static final String ACCESS_KEY_ID = "AKIAJ3W2NBVOWKCXES7Q";
	public static final String SECRET_KEY = "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";
	
	private static Context c;
	private static LocationListener locationListener;
	private static LocationManager lm;
	
	private AmazonS3Client client = new AmazonS3Client(new BasicAWSCredentials(ACCESS_KEY_ID, SECRET_KEY));
	private Boolean locationRefreshed;
	private String fileName;
	private View rView;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		rView = inflater.inflate(R.layout.fragment_data_info, container, false);
		
		// Initialize fragment objects
		c = container.getContext();
		lm = (LocationManager) c.getSystemService(Context.LOCATION_SERVICE);
		locationListener = new LocationListener() {
    		public void onLocationChanged(Location location) {
    			String lat = new BigDecimal(location.getLatitude()).setScale(6, RoundingMode.HALF_EVEN).toString();
    			String lng = new BigDecimal(location.getLongitude()).setScale(6, RoundingMode.HALF_EVEN).toString();
    			
    			updatePositionDataUnit(lat, lng);
    			lm.removeUpdates(locationListener);
    			locationRefreshed = Boolean.valueOf(true);
    		}
			public void onStatusChanged(String provider, int status, Bundle extras) {}
    		public void onProviderEnabled(String provider) { }
    		public void onProviderDisabled(String provider) { }
    	};
		// view initializer?
		rView.findViewById(R.id.sec3_01_row01_button).setOnClickListener(this);
		
		return rView;
	}
	
	public void onClick(View view) {
		switch (view.getId()) {
        case R.id.sec3_01_row01_button:
        	Spinner select = (Spinner) rView.findViewById(R.id.sec3_01_row01_spinner);
        	fileName = (String) select.getSelectedItem() + "file.txt";
        	// GPS prop
        	if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
        		updatePositionDataUnit("200.0", "200.0");
        		//updatePositionDisplay("GPS Disabled!");
        		new DownloadSpeedTest().execute(fileName); 
			} else new UpdatePhoneLocation().execute();
            break;
        }
	}
	
	public void updatePositionDataUnit(String lat, String lng) {
		// add DataUnit objects for Latitude, Longitude
		if (DataUnitHandler.getInstance().addDataUnit("DvcLat", lat, "N", true)) {
			if (DataUnitHandler.getInstance().addDataUnit("DvcLon", lng, "N", true)) ;
			else {
				Log.e("GPSInfoFragment", "Failed adding longitude to dataset, clearing GPS datapoints.");
				DataUnitHandler.getInstance().removeDataUnit("DvcLat");
				DataUnitHandler.getInstance().removeDataUnit("DvcLon");
			}
		} else {
			Log.e("GPSInfoFragment", "Failed adding latitude to dataset, clearing GPS datapoints.");
			DataUnitHandler.getInstance().removeDataUnit("DvcLat");
			DataUnitHandler.getInstance().removeDataUnit("DvcLon");
			updatePositionDataUnit(lat, lng);
		}
	}
	
	class UpdatePhoneLocation extends AsyncTask<Void, Void, TaskResult> {
		
		ProgressDialog dialog;
		
		@Override
		protected void onPreExecute() {
			locationRefreshed = Boolean.valueOf(false);
			dialog = new ProgressDialog(getActivity());
    	    dialog.setMessage(getString(R.string.sec1_dialog_gps_update));
    	    dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    	    dialog.setCancelable(false);
    	    dialog.show();
    	    lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
		}

		@Override
		protected TaskResult doInBackground(Void... arg0) {
			//TODO force location update
			TaskResult result = new TaskResult();
			int n = 0;
			
			try {
				while (!locationRefreshed.booleanValue() && n < 15) {
					n++;
					SystemClock.sleep(1000);
				}
				
				if (!locationRefreshed.booleanValue()) {
					result.setFeedback("Old location!");
					updatePositionDataUnit("200.0", "201.0");
					result.taskFail();
				} else {
					result.setFeedback("Success!");
					result.taskSuccess();
				}
				
				lm.removeUpdates(locationListener);
				return result;
			} catch (Throwable e) {
				lm.removeUpdates(locationListener);
				result.setFeedback(e.getMessage() + ": " + e.getStackTrace().toString());
				result.taskFail();
			}
			return result;
		}
		
		@Override
		protected void onPostExecute(TaskResult result) {
			//updatePositionDisplay(result.getFeedback());
			dialog.dismiss();
			new DownloadSpeedTest().execute(fileName);
		}
		
	}
	
	class DownloadSpeedTest extends AsyncTask<String, Integer, AsyncTaskResult> {
		AsyncTaskResult result = new AsyncTaskResult();
		Double dlSpeed, ulSpeed;
    	ProgressDialog dlDialog;
    	String filename;
    	
    	@Override
    	protected void onPreExecute() {
    	    // Set up progress dialog
    		dlDialog = new ProgressDialog(getActivity());
    		dlDialog.setMessage("Download Speed Test In Progress");
    		dlDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
    		dlDialog.setCancelable(false);
    		dlDialog.show();
    	}
    	
    	@Override
    	protected AsyncTaskResult doInBackground(String... inString) {
    		if (inString == null || inString.length != 1)
    			return null;
    		
    		filename = inString[0];
    		File inFile = new File(rView.getContext().getFilesDir(), inString[0]);
    		dlDialog.setMax(getFileSize(filename));
    		
			client.setEndpoint("s3.amazonaws.com");
			result.setSuccess(downloadFile(result, inFile));
    		addData();
			return result;
    	}
    	
    	private int getFileSize(String filename) {
    		int size = Integer.parseInt(filename.substring(0, filename.length()-10));
    		switch(size) {
    		case 1:
    			return size*1024*1024;
    		case 250:
    			return size*1024;
    		default:
    			return 1;
    		}
    	}
    	
    	private boolean downloadFile(AsyncTaskResult result, File file) {
			try {
				GetObjectRequest gor = new GetObjectRequest(S3_BUCKET + S3_DOWNLOAD, file.getName());
				gor.setProgressListener(new ProgressListener() {
				    int total = 0;
				 
				    @Override
				    public void progressChanged(ProgressEvent pv) {
				        total += (int) pv.getBytesTransferred();
				        publishProgress(total);
				    }
				});
				
				Date startTime = new Date();
				client.getObject(gor, file);
				Date endTime = new Date();
				
				// collect stats
				double time = (endTime.getTime() - startTime.getTime()) / 1000.0;
				dlSpeed = new BigDecimal((file.length()/1024.0)/time).setScale(2, RoundingMode.DOWN).doubleValue();
				result.setOutput(result.getOutput() + "\n Download rate: " + dlSpeed + " kBps");
			} catch(Throwable e) {
				dlSpeed = -1.0;
				result.setErrorMessage(result.getErrorMessage() + "\n" + e.getMessage());
				return false;
			}
			return true;
		}
    	
    	private boolean addData() {
    		boolean added = true;
    		if (added && DataUnitHandler.getInstance().addDataUnit("DLSTR", dlSpeed.toString(), "N", true));
    		else added = false;
    		
    		if (!added) {
    			Log.e("DataInfoFragment", "Failed loading Download Speed Test data, clearing & forcing retry.");
    			while (DataUnitHandler.getInstance().removeDataUnit("DLSTR"));
    			return addData();
    		} else return added;
    	}
    	
    	protected void onProgressUpdate(Integer... values) {
		    // Update the progress bar
    		dlDialog.setProgress(values[0]);
		}
    	
    	protected void onPostExecute(AsyncTaskResult result) {
    		TextView label = (TextView) rView.findViewById(R.id.sec3_01_row01_result);
			TextView body = (TextView) rView.findViewById(R.id.sec3_error_result);
			
			if (result.isSuccess()) {
				label.setText("File got!");
				body.setText(result.getOutput());
			} else {
				label.setText("ERROR!");
				body.setText(result.getOutput() + "\n\n" + result.getErrorMessage());
			}
			dlDialog.dismiss();
			new UploadSpeedTest().execute(filename);
		}
	}
	
	class UploadSpeedTest extends AsyncTask<String, Integer, AsyncTaskResult> {
		AsyncTaskResult result = new AsyncTaskResult();
    	ProgressDialog ulDialog;
    	Double dlSpeed, ulSpeed;
    	
    	@Override
    	protected void onPreExecute() {
    	    // Set up progress dialog
    		ulDialog = new ProgressDialog(getActivity());
    		ulDialog.setMessage("Upload Speed Test In Progress");
    		ulDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
    		ulDialog.setCancelable(false);
    		ulDialog.show();
    	}
    	
    	@Override
    	protected AsyncTaskResult doInBackground(String... inString) {
    		if (inString == null || inString.length != 1)
    			return null;
    		
    		File inFile = new File(rView.getContext().getFilesDir(), inString[0]);
    		ulDialog.setMax(Long.valueOf(inFile.length()).intValue());
    		
			client.setEndpoint("s3.amazonaws.com");
    		result.setSuccess(uploadFile(result, inFile));
    		addData();
			return result;
    	}
    	
    	private boolean uploadFile(AsyncTaskResult result, File file) {
    		try {
	    		PutObjectRequest por = new PutObjectRequest(S3_BUCKET + S3_UPLOAD, file.getName(), file);
	    		por.setProgressListener(new ProgressListener() {
				    int total = 0;
				 
				    @Override
				    public void progressChanged(ProgressEvent pv) {
				        total += (int) pv.getBytesTransferred();
				        publishProgress(total);
				    }
				});
	    		
	    		// send file and track duration
	    		Date startTime = new Date();
	    		client.putObject(por);
	    		Date endTime = new Date();
	    		
	    		// collect stats
	    		double time = (endTime.getTime() - startTime.getTime()) / 1000.0;
	    		ulSpeed = new BigDecimal((file.length()/1024.0)/time).setScale(2, RoundingMode.DOWN).doubleValue();
	    		result.setOutput(result.getOutput() + "\n Upload rate: " + ulSpeed + " kBps");
    		} catch (Throwable e) {
    			ulSpeed = -1.0;
    			result.setErrorMessage(result.getErrorMessage() + "\n" + e.getMessage());
    			return false;
    		}
    		return true;
    	}
    	
    	private boolean addData() {
    		boolean added = true;
    		if (added && DataUnitHandler.getInstance().addDataUnit("ULSTR", ulSpeed.toString(), "N", true)); 
    		else added = false;
    		
    		if (!added) {
    			Log.e("DataInfoFragment", "Failed loading Upload Speed Test data, clearing & forcing retry.");
    			while (DataUnitHandler.getInstance().removeDataUnit("ULSTR"));
    			return addData();
    		} else return added;
    	}
    	
    	protected void onProgressUpdate(Integer... values) {
		    // Update the progress bar
    		ulDialog.setProgress(values[0]);
		}
    	
    	protected void onPostExecute(AsyncTaskResult result) {
    		TextView label = (TextView) rView.findViewById(R.id.sec3_01_row01_result);
			TextView body = (TextView) rView.findViewById(R.id.sec3_error_result);
			
			if (result.isSuccess()) {
				label.setText("File sent!");
				body.setText(body.getText() + result.getOutput());
			} else {
				label.setText("ERROR!");
				body.setText(result.getOutput() + "\n\n" + result.getErrorMessage());
			}
			ulDialog.dismiss();
			new DynamoDBTask().execute();
		}
	}
	
	class DynamoDBTask extends AsyncTask<Void, Void, AsyncTaskResult> {
		AsyncTaskResult result = new AsyncTaskResult();
    	ProgressDialog dbDialog;
    	
    	@Override
    	protected void onPreExecute() {
    	    // Set up progress dialog
    		dbDialog = new ProgressDialog(getActivity());
    		dbDialog.setMessage("Sending to Dynamo DB");
    		dbDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    		dbDialog.setCancelable(false);
    	    dbDialog.show();
    	}
    	
    	@Override
    	protected AsyncTaskResult doInBackground(Void... void0) {
    		SystemDataHandler.getInstance().loadDataPoint(); 
    		ServiceDataHandler.getInstance().loadDataPoint();
    		result.setSuccess(DynamoDBHandler.loadData("CellSpeedData2013001"));
			return result;
    	}
    	
    	protected void onPostExecute(AsyncTaskResult result) {
			TextView label = (TextView) rView.findViewById(R.id.sec3_01_row01_result);
			TextView body = (TextView) rView.findViewById(R.id.sec3_error_result);
			
			if (result.isSuccess()) {
				label.setText("Data sent!");
				body.setText(body.getText() + result.getOutput());
			} else {
				label.setText("ERROR!");
				body.setText(result.getOutput() + "\n\n" + result.getErrorMessage());
			}
			dbDialog.dismiss();
		}
	}
	
	private class AsyncTaskResult {
    	private Boolean success = false;
		private String errorMessage = "";
		private String output = "";

		public Boolean isSuccess() { return success; }
		public void setSuccess(Boolean value) { this.success = value; }
		
		public String getErrorMessage() { return errorMessage; }
		public void setErrorMessage(String value) { this.errorMessage = value; }
		
		public String getOutput() { return output; }
		public void setOutput(String value) { this.output = value; }
	}

}
