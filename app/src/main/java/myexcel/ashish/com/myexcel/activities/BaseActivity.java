package myexcel.ashish.com.myexcel.activities;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import myexcel.ashish.com.myexcel.R;

/**
 * Created by Ashish Goel on 1/3/2016.
 */
public class BaseActivity extends AppCompatActivity {

    public Toolbar toolbar;
    int toolbarHeight;

    ProgressBar progressBar;
    LinearLayout progressBarContainer;

    LinearLayout connectionErrorLayout;
    LinearLayout retryDataConnectionLayout;
    LinearLayout connectionFailedCloudImage;
    Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    void setProgressLayoutVariablesAndErrorVariables() {
        progressBar = (ProgressBar) findViewById(R.id.progressbarloading);
        progressBarContainer = (LinearLayout) findViewById(R.id.progressbarcontainer);

        connectionErrorLayout = (LinearLayout) findViewById(R.id.connection_error_layout);
        retryDataConnectionLayout = (LinearLayout) findViewById(R.id.retrylayoutconnectionerror);
        connectionFailedCloudImage = (LinearLayout) findViewById(R.id.connectionfailedimagelayout);
    }

    void showLoadingLayout() {
        progressBar.setVisibility(View.VISIBLE);
        progressBarContainer.setVisibility(View.VISIBLE);
    }

    void hideLoadingLayout() {
        progressBar.setVisibility(View.GONE);
        progressBarContainer.setVisibility(View.GONE);
    }

    void showErrorLayout() {
        connectionErrorLayout.setVisibility(View.VISIBLE);

        retryDataConnectionLayout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

            }
        });

        connectionFailedCloudImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                retryDataConnectionLayout.performClick();
            }
        });
        findViewById(R.id.opensettingslayout).setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(
                                Settings.ACTION_WIFI_SETTINGS);
                        if (intent.resolveActivity(getPackageManager()) != null) {
                            startActivity(intent);
                        }
                    }
                });
    }

    void hideErrorLayout() {
        if (connectionErrorLayout != null)
            connectionErrorLayout.setVisibility(View.GONE);
    }

    public void makeToast(String text) {
        if (toast != null)
            toast.cancel();
        if (this != null) {
            toast = Toast.makeText(this, text, Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    public void showSnackBar(String text) {
//        if (findViewById(R.id.coordinatorlayout) != null)
//            Snackbar.make(findViewById(R.id.coordinatorlayout), text,
//                    Snackbar.LENGTH_SHORT).show();
    }
}
