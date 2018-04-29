package app.skirmantas.info.warframes_codex.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import app.skirmantas.info.warframes_codex.R;
import app.skirmantas.info.warframes_codex.app.Prefs;
import app.skirmantas.info.warframes_codex.model.Warframes;
import app.skirmantas.info.warframes_codex.realm.RealmController;
import io.realm.Realm;
import io.realm.RealmResults;

public class WarframesAdapter extends RealmRecyclerViewAdapter<Warframes> {

    final Context context;
    private Realm realm;
    private LayoutInflater inflater;

    public WarframesAdapter(Context context) {

        this.context = context;
    }

    // create new views (invoked by the layout manager)
    @Override
    public CardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // inflate a new card view
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_warframes, parent, false);
        return new CardViewHolder(view);
    }

    // replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {

        realm = RealmController.getInstance().getRealm();

        // get the article
        final Warframes warframes = getItem(position);
        // cast the generic view holder to our specific one
        final CardViewHolder holder = (CardViewHolder) viewHolder;

        // set the title and the snippet
        holder.textTitle.setText(warframes.getTitle());
        holder.textHealth.setText(warframes.getHealth());
        holder.textEnergy.setText(warframes.getEnergy());
        holder.textDescription.setText(warframes.getDescription());

        // load the background image
        if (warframes.getImageUrl() != null) {
            Glide.with(context)
                    .load(warframes.getImageUrl().replace("https", "http"))
                    .asBitmap()
                    .fitCenter()
                    .into(holder.imageBackground);
        }

        //remove single match from realm
        holder.card.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                RealmResults<Warframes> results = realm.where(Warframes.class).findAll();

                // Get the warframes title to show it in toast message
                Warframes b = results.get(position);
                String title = b.getTitle();

                // All changes to data must happen in a transaction
                realm.beginTransaction();

                // remove single match
                results.remove(position);
                realm.commitTransaction();

                if (results.size() == 0) {
                    Prefs.with(context).setPreLoad(false);
                }

                notifyDataSetChanged();

                Toast.makeText(context, title + " is removed from Realm", Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        //update single match from realm
        holder.card.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View content = inflater.inflate(R.layout.edit_item, null);
                final EditText editTitle = (EditText) content.findViewById(R.id.title);
                final EditText editHealth = (EditText) content.findViewById(R.id.health);
                final EditText editEnergy = (EditText) content.findViewById(R.id.energy);
                final EditText editThumbnail = (EditText) content.findViewById(R.id.thumbnail);

                editTitle.setText(warframes.getTitle());
                editHealth.setText(warframes.getHealth());
                editEnergy.setText(warframes.getEnergy());
                editThumbnail.setText(warframes.getImageUrl());

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setView(content)
                        .setTitle("Edit Warframes")
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                RealmResults<Warframes> results = realm.where(Warframes.class).findAll();

                                realm.beginTransaction();
                                results.get(position).setTitle(editTitle.getText().toString());
                                results.get(position).setHealth(editHealth.getText().toString());
                                results.get(position).setEnergy(editEnergy.getText().toString());
                                results.get(position).setImageUrl(editThumbnail.getText().toString());

                                realm.commitTransaction();

                                notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }

    // return the size of your data set (invoked by the layout manager)
    public int getItemCount() {

        if (getRealmAdapter() != null) {
            return getRealmAdapter().getCount();
        }
        return 0;
    }

    public static class CardViewHolder extends RecyclerView.ViewHolder {

        public CardView card;
        public TextView textTitle;
        public TextView textHealth;
        public TextView textEnergy;
        public TextView textDescription;
        public ImageView imageBackground;

        public CardViewHolder(View itemView) {
            // standard view holder pattern with Butterknife view injection
            super(itemView);

            card = (CardView) itemView.findViewById(R.id.card_warframes);
            textTitle = (TextView) itemView.findViewById(R.id.text_warframes_name);
            textHealth = (TextView) itemView.findViewById(R.id.text_warframes_health);
            textEnergy = (TextView) itemView.findViewById(R.id.text_warframes_energy);
            textDescription = (TextView) itemView.findViewById(R.id.text_books_description);
            imageBackground = (ImageView) itemView.findViewById(R.id.image_background);
        }
    }
}
