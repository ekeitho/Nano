package it.jaschke.alexandria.CameraPreview;

/*
 * Barebones implementation of displaying camera preview.
 *
 * Created by lisah0 on 2012-02-24
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.zxing.Result;

import java.util.List;

import it.jaschke.alexandria.AddBook;
import it.jaschke.alexandria.R;
import it.jaschke.alexandria.services.BookService;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

/** A basic Camera preview class */
public class CameraPreview extends Fragment implements ZXingScannerView.ResultHandler{

    private ZXingScannerView mScannerView;
    private ViewGroup viewGroup;
    private boolean rotation = false;

    @Override
    public void onSaveInstanceState(Bundle outState) {
        rotation = true;
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mScannerView = new ZXingScannerView(getActivity());
        mScannerView.setResultHandler(this);
        viewGroup = container;
        viewGroup.addView(mScannerView);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onResume() {
        mScannerView.startCamera();
        super.onResume();
    }

    // ondestroy gets called if the user opens up scanner and clicks back or when the scanner
    // recognizes a barcode

    // stop the camera
    // remove it from the parent view
    // remove from fragment backstack
    @Override
    public void onDestroy() {
        mScannerView.stopCamera();
        viewGroup.removeView(mScannerView);

        // dont pop off stack if it's a rotation destroy
        if (!rotation) {
            getFragmentManager().beginTransaction().remove(this).commit();
        }
        super.onDestroy();
    }

    @Override
    public void handleResult(Result result) {
        // had to use the replace(..,.., tag) instead of just doing addToBackStack(tag)
        AddBook frag = (AddBook) getFragmentManager().findFragmentByTag(getString(R.string.scan));
        if (frag != null) {
            frag.startBookService(result.getText());
        }
        onDestroy();
    }

}
