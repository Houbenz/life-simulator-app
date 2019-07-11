package Workers;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.Player;
import com.google.android.gms.games.SnapshotsClient;
import com.google.android.gms.games.snapshot.Snapshot;
import com.google.android.gms.games.snapshot.SnapshotMetadata;
import com.google.android.gms.games.snapshot.SnapshotMetadataChange;
import com.google.android.gms.tasks.Task;
import com.houbenz.lifesimulator.MainMenu;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;
import database.Acquired_Cars;
import database.Acquired_Furnitures;
import database.Acquired_Houses;
import database.Acquired_Stores;
import database.Acquired_degree;
import database.Gift;
import database.Partner;

import static com.houbenz.lifesimulator.MainMenu.myAppDataBase;

public class SaveToCloudWork extends Worker {



    private static final String  currentSave="snapshot"+1235;
    private GoogleSignInAccount account;

    public SaveToCloudWork(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);

    }

    @NonNull
    @Override
    public Result doWork() {

        account = GoogleSignIn.getLastSignedInAccount(getApplicationContext());

        if (account != null && GoogleSignIn.hasPermissions(account)){
            executeSaving(getBytearray());
        Log.i("YOOU", "saving operation done");

        }

        else
            Log.i("YOOU","account is null or does not have permission");

        return Result.success();
    }




    private void executeSaving(byte[] bytearray){

        SnapshotsClient snapshotsClient= Games.getSnapshotsClient(getApplicationContext(),account);

        // to save a game
        snapshotsClient.open(currentSave,true).addOnCompleteListener(task -> {

            database.Player player =MainMenu.myAppDataBase.myDao().getPlayer(1);
            Snapshot snapshot =task.getResult().getData();

            if (snapshot != null){

                //call for write snapshot method returns a task !
                writeSnapshot(snapshot,bytearray,player.getName()).addOnCompleteListener(task1 -> {

                    if(task1.isSuccessful()){
                        Toast.makeText(getApplicationContext(),"saved to cloud",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private Task<SnapshotMetadata> writeSnapshot(@NonNull Snapshot snapshot , byte[] bytearray, String desc) {

        snapshot.getSnapshotContents().writeBytes(bytearray);

        SnapshotMetadataChange snapshotMetadata = new SnapshotMetadataChange.Builder()
                .setDescription(desc)
                .build();

        SnapshotsClient snapshotsClient = Games.getSnapshotsClient(getApplicationContext(), account);

        return snapshotsClient.commitAndClose(snapshot, snapshotMetadata);
    }

    private byte[] getBytearray(){
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            ObjectOutputStream oos = new ObjectOutputStream(bos);

            database.Player player=myAppDataBase.myDao().getPlayer(1);
            List<Acquired_Furnitures> acquired_furnitures = myAppDataBase.myDao().getAcquiredFurnitures(1);
            List<Acquired_degree> acquired_degrees =myAppDataBase.myDao().getAcquiredDegrees(1);
            List<Acquired_Cars> acquired_cars =myAppDataBase.myDao().getAcquiredCars(1);
            List<Acquired_Houses> acquired_houses =myAppDataBase.myDao().getAcquiredHouses(1);
            List<Acquired_Stores> acquired_stores =myAppDataBase.myDao().getAcquiredStores(1);
            List<Gift> gifts = myAppDataBase.myDao().getGifts();
            List<Partner> partners =myAppDataBase.myDao().getPartners();

            oos.writeObject(player);
            oos.writeObject(acquired_furnitures);
            oos.writeObject(acquired_degrees);
            oos.writeObject(acquired_cars);
            oos.writeObject(acquired_houses);
            oos.writeObject(acquired_stores);
            oos.writeObject(partners);
            oos.writeObject(gifts);

            bos.toByteArray();
            oos.close();


        } catch (IOException e) {
            e.printStackTrace();
        }

        return bos.toByteArray();
    }
}
