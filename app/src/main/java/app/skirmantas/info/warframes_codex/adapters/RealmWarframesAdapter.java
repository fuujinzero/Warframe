package app.skirmantas.info.warframes_codex.adapters;

import android.content.Context;

import app.skirmantas.info.warframes_codex.model.Warframes;
import io.realm.RealmResults;

public class RealmWarframesAdapter extends RealmModelAdapter<Warframes> {

    public RealmWarframesAdapter(Context context, RealmResults<Warframes> realmResults, boolean automaticUpdate) {

        super(context, realmResults, automaticUpdate);
    }
}