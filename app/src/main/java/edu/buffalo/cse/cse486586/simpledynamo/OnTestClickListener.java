package edu.buffalo.cse.cse486586.simpledynamo;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class OnTestClickListener implements OnClickListener {

	private static final String TAG = OnTestClickListener.class.getName();
	private static final int TEST_CNT = 50;
	private static final String KEY_FIELD = "key";
	private static final String VALUE_FIELD = "value";

	private final TextView mTextView;
	private final ContentResolver mContentResolver;
	private final Uri mUri;
	private final ContentValues[] mContentValues;


	public OnTestClickListener(TextView _tv, ContentResolver _cr) {
		mTextView = _tv;
		mContentResolver = _cr;
		mUri = buildUri("content", "edu.buffalo.cse.cse486586.simpledynamo.provider");
		mContentValues = initTestValues();
	}

	private Uri buildUri(String scheme, String authority) {
		Uri.Builder uriBuilder = new Uri.Builder();
		uriBuilder.authority(authority);
		uriBuilder.scheme(scheme);
		return uriBuilder.build();
	}

	private ContentValues[] initTestValues() {
		ContentValues[] cv = new ContentValues[TEST_CNT];
		for (int i = 0; i < TEST_CNT; i++) {
			cv[i] = new ContentValues();
			cv[i].put(KEY_FIELD, "key" + Integer.toString(i));
			cv[i].put(VALUE_FIELD, "val" + Integer.toString(i));
		}

		return cv;
	}

	@Override
	public void onClick(View v) {
		new Task().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
	}

	private class Task extends AsyncTask<Void, String, Void> {

		@Override
		protected Void doInBackground(Void... params) {
//			if (testInsert()) {
//				publishProgress("Insert success\n");
//			} else {
//				publishProgress("Insert fail\n");
//				return null;
//			}

			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

//			if (testQuery()) {
//				publishProgress("Query success\n");
//			} else {
//				publishProgress("Query fail\n");
//			}

			String s = testDelete();
            publishProgress(s+"\n");


			return null;
		}
		
		protected void onProgressUpdate(String...strings) {
			mTextView.append(strings[0]);

			return;
		}

		private String testDelete() {
			try {


					int deleteFlag = mContentResolver.delete(mUri, "all", null) ;
					if (deleteFlag==1){
					    return "Deleted All";
                    }

			} catch (Exception e) {
				Log.e(TAG, e.toString());
			}
            return "not Deleted";

		}

		private boolean testInsert() {
			try {
				for (int i = 0; i < TEST_CNT; i++) {
					String key = (String) mContentValues[i].get(KEY_FIELD);
					String val = (String) mContentValues[i].get(VALUE_FIELD);
					Log.e(TAG, key +" "+ val);

					mContentResolver.insert(mUri, mContentValues[i]);
				}
			} catch (Exception e) {
				Log.e(TAG, e.toString());
				return false;
			}

			return true;
		}

		private boolean testQuery() {
			try {
				for (int i = 0; i < 1; i++) {
					String key = (String) mContentValues[i].get(KEY_FIELD);
					String val = (String) mContentValues[i].get(VALUE_FIELD);

					Cursor resultCursor = mContentResolver.query(mUri, null,
							"@", null, null);


					if (resultCursor == null) {
						Log.e(TAG, "Result null");
						throw new Exception();
					}

					int keyIndex = resultCursor.getColumnIndex(KEY_FIELD);
					int valueIndex = resultCursor.getColumnIndex(VALUE_FIELD);
					if (keyIndex == -1 || valueIndex == -1) {
						Log.e(TAG, "Wrong columns");
						resultCursor.close();
						throw new Exception();
					}

					resultCursor.moveToFirst();
					MatrixCursor tempCur = new MatrixCursor(new String[]{"key", "value"});
					tempCur.addRow(new Object[]{resultCursor.getString(keyIndex), resultCursor.getString(valueIndex)});
					tempCur.moveToFirst();

					if (!(tempCur.isFirst() && tempCur.isLast())) {
						Log.e(TAG, "Wrong number of rows");
						tempCur.close();
						throw new Exception();
					}

					keyIndex = tempCur.getColumnIndex(KEY_FIELD);
					valueIndex = tempCur.getColumnIndex(VALUE_FIELD);


//					if (!(tempCur.getString(keyIndex).equals(key) && tempCur.getString(valueIndex).equals(val))) {
//						Log.e(TAG, "(key, value) pairs don't match\n");
//						tempCur.close();
//						throw new Exception();
//					}

					while(resultCursor.moveToNext()){

						MatrixCursor tempCur2 = new MatrixCursor(new String[]{"key", "value"});
						tempCur2.addRow(new Object[]{resultCursor.getString(keyIndex), resultCursor.getString(valueIndex)});
						tempCur2.moveToFirst();

						if (!(tempCur2.isFirst() && tempCur2.isLast())) {
							Log.e(TAG, "Wrong number of rows");
							tempCur2.close();
							throw new Exception();
						}

						keyIndex = tempCur2.getColumnIndex(KEY_FIELD);
						valueIndex = tempCur2.getColumnIndex(VALUE_FIELD);

//						if (!(tempCur2.getString(keyIndex).equals(key) && tempCur2.getString(valueIndex).equals(val))) {
//							Log.e(TAG, "(key, value) pairs don't match\n");
//							tempCur2.close();
//							throw new Exception();
//						}


					}

					resultCursor.close();
				}
			} catch (Exception e) {
				return false;
			}

			return true;
		}
	}
}
