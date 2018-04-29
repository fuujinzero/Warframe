package app.skirmantas.info.warframes_codex.realm;


import android.app.Activity;
import android.app.Application;
import android.support.v4.app.Fragment;

import app.skirmantas.info.warframes_codex.model.Warframes;
import io.realm.Realm;
import io.realm.RealmResults;


public class RealmController {

    private static RealmController instance;
    private final Realm realm;

    public RealmController(Application application) {
        realm = Realm.getDefaultInstance();
    }

    public static RealmController with(Fragment fragment) {
        if (instance == null) {
            instance = new RealmController(fragment.getActivity().getApplication());
        }
        return instance;
    }

    public static RealmController with(Activity activity) {
        if (instance == null) {
            instance = new RealmController(activity.getApplication());
        }
        return instance;
    }

    public static RealmController with(Application application) {
        if (instance == null) {
            instance = new RealmController(application);
        }
        return instance;
    }

    public static RealmController getInstance() {
        return instance;
    }

    public Realm getRealm() {
        return realm;
    }

    //Refresh the realm istance
    public void refresh() {
        realm.refresh();
    }

    //clear all objects from Warframes.class
    public void clearAll() {
        realm.beginTransaction();
        realm.clear(Warframes.class);
        realm.commitTransaction();
    }

    //find all objects in the Warframes.class
    public RealmResults<Warframes> getWarframes() {
        return realm.where(Warframes.class).findAll();
    }

    //query a single item with the given id
    public Warframes getWarframe(String id) {
        return realm.where(Warframes.class).equalTo("id", id).findFirst();
    }

    //check if Warframes.class is empty
    public boolean hasWarframes() {
        return !realm.allObjects(Warframes.class).isEmpty();
    }

    //query example
    public RealmResults<Warframes> queryedWarframe() {

        return realm.where(Warframes.class)
                .contains("name", "name 0")
                .or()
                .contains("health", "Realm")
                .findAll();

    }
}
