package dk.au.mad21fall.appproject.group3;

import android.net.Uri;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class DownloadUrl {
    Uri downloaduri;

    //https://firebase.google.com/docs/storage/android/download-files#download_data_via_url
    public Uri getUrlAsync (String date){
        // Points to the root reference

        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        StorageReference dateRef = storageRef.child("/" + date+ "png");
        dateRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>()
        {
            @Override
            public void onSuccess(Uri downloadUrl)
            {
                downloaduri = downloadUrl;
            }
        });
        return downloaduri;
    }

}
