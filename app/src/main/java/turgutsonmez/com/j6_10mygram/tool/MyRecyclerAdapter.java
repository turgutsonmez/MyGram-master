package turgutsonmez.com.j6_10mygram.tool;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.like.LikeButton;
import com.like.OnLikeListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import turgutsonmez.com.j6_10mygram.R;
import turgutsonmez.com.j6_10mygram.model.Gonderi;
import turgutsonmez.com.j6_10mygram.model.Uye;

/**
 * Created by Mesut on 18.09.2017.
 */

public class MyRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

  public static final int STANDART_MENU_ITEM = 0;
  public static final int AD_ITEM = 1;

  private FirebaseDatabase database;
  private DatabaseReference myRef;
  private ArrayList<Gonderi> gonderiler;
  private Context context;
  private myOnClickListener onClickListener;

  public MyRecyclerAdapter(Context context, ArrayList<Gonderi> gonderiler, myOnClickListener onClickListener) {
    this.gonderiler = gonderiler;
    this.context = context;
    this.onClickListener = onClickListener;
  }

  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    RecyclerView.ViewHolder viewHolder = null;
    LayoutInflater inflater = LayoutInflater.from(parent.getContext());
    switch (viewType) {
      case STANDART_MENU_ITEM:
        View vv = inflater.inflate(R.layout.card_item, parent, false);
        viewHolder = new MyViewHolder(vv);
        final MyViewHolder myViewHolder = (MyViewHolder) viewHolder;
        myViewHolder.btnMenu.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            onClickListener.onItemClick(view, myViewHolder.getPosition());
          }
        });
        break;
      case AD_ITEM:
        View v2 = inflater.inflate(R.layout.native_ad_container, parent, false);
        viewHolder = new ViewHolderAdMob(v2);
        break;
    }
    return viewHolder;

        /*View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item, parent, false);
        final MyViewHolder holder = new MyViewHolder(view);
        holder.btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickListener.onItemClick(view, holder.getPosition());
            }
        });
        return holder;*/
  }

  @Override
  public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
    database = FirebaseDatabase.getInstance();
    myRef = database.getReference().child("posts");
    switch (holder.getItemViewType()) {
      case STANDART_MENU_ITEM:
        final MyViewHolder viewHolder = (MyViewHolder) holder;
        final Query query = myRef.child(gonderiler.get(position).getId());
        query.addValueEventListener(new ValueEventListener() {
          @Override
          public void onDataChange(final DataSnapshot dataSnapshot) {
            Gonderi gelen = dataSnapshot.getValue(Gonderi.class);
            resimGoruntulenmeArttir(gelen.getId());
            Picasso.with(context).load(gelen.getYol()).into(viewHolder.imgFoto);
            viewHolder.txtGosterim.setText(String.format("Beğeni: 0 Gösterim: %s", gelen.getGoruntulenme()));
            viewHolder.btnLike.setLiked(false);
            if (gelen.getLikes() != null && gelen.getLikes().size() > 0) {
              viewHolder.txtGosterim.setText(String.format("Beğeni: %s Gösterim: %s", gelen.getLikes().size(), gelen.getGoruntulenme()));
              String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
              for (int i = 0; i < gelen.getLikes().size(); i++) {
                if (gelen.getLikes().get(i).equals(uid)) {
                  viewHolder.btnLike.setLiked(true);
                  break;
                }
              }

            }


            final DatabaseReference dbref = database.getReference().child("uyeler").child(gelen.getGonderenId());
            dbref.addValueEventListener(new ValueEventListener() {
              @Override
              public void onDataChange(DataSnapshot dataSnapshot) {
                Uye uye = dataSnapshot.getValue(Uye.class);
                viewHolder.txtAd.setText(uye.getAd());
                Picasso.with(context).load(uye.getFotograf()).into(viewHolder.imgProfil);
                dbref.removeEventListener(this);
              }

              @Override
              public void onCancelled(DatabaseError databaseError) {

              }
            });


            query.removeEventListener(this);
          }

          @Override
          public void onCancelled(DatabaseError databaseError) {

          }
        });
        final int pos = position;
        viewHolder.btnLike.setOnLikeListener(new OnLikeListener() {
          @Override
          public void liked(LikeButton likeButton) {
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("posts");
            ref = ref.child(gonderiler.get(pos).getId());
            final DatabaseReference finalRef = ref;
            finalRef.addValueEventListener(new ValueEventListener() {
              @Override
              public void onDataChange(DataSnapshot dataSnapshot) {
                Gonderi gg = dataSnapshot.getValue(Gonderi.class);
                if (gg.getLikes() == null)
                  gg.setLikes(new ArrayList<String>());
                ArrayList<String> likes = gg.getLikes();
                likes.add(FirebaseAuth.getInstance().getCurrentUser().getUid());
                gg.setLikes(likes);
                finalRef.setValue(gg);
                finalRef.removeEventListener(this);
              }

              @Override
              public void onCancelled(DatabaseError databaseError) {

              }
            });
          }

          @Override
          public void unLiked(LikeButton likeButton) {
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("posts");
            ref = ref.child(gonderiler.get(pos).getId());
            final DatabaseReference finalRef = ref;
            finalRef.addValueEventListener(new ValueEventListener() {
              @Override
              public void onDataChange(DataSnapshot dataSnapshot) {
                Gonderi gg = dataSnapshot.getValue(Gonderi.class);
                ArrayList<String> likes = gg.getLikes();
                likes.remove(FirebaseAuth.getInstance().getCurrentUser().getUid());
                gg.setLikes(likes);
                finalRef.setValue(gg);
                finalRef.removeEventListener(this);
              }

              @Override
              public void onCancelled(DatabaseError databaseError) {

              }
            });
          }
        });
        break;
      default:
        break;
    }

  }

  private void resimGoruntulenmeArttir(final String resimId) {
    database = FirebaseDatabase.getInstance();
    myRef = database.getReference().child("posts");
    final Query query = myRef.child(resimId);
    query.addValueEventListener(new ValueEventListener() {
      @Override
      public void onDataChange(DataSnapshot dataSnapshot) {
        Gonderi gonderi = dataSnapshot.getValue(Gonderi.class);
        ArrayList<String> bakanlar = gonderi.getBakanlar();
        if (gonderi.getBakanlar() == null)
          bakanlar = new ArrayList<>();
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        boolean bakmisMi = false;
        for (int i = 0; i < bakanlar.size(); i++) {
          if (gonderi.getBakanlar().get(i).equals(uid)) {
            bakmisMi = true;
            break;
          }
        }
        if (!bakmisMi) {
          bakanlar.add(uid);
          gonderi.setBakanlar(bakanlar);
          gonderi.setGoruntulenme(gonderi.getGoruntulenme() + 1);
          myRef = database.getReference().child("posts").child(resimId);
          myRef.setValue(gonderi);
        }
        query.removeEventListener(this);
      }

      @Override
      public void onCancelled(DatabaseError databaseError) {

      }
    });
  }

  @Override
  public int getItemViewType(int position) {
    //hertarihdegistiginde
    return (position % 5 == 1) ? AD_ITEM : STANDART_MENU_ITEM;
  }

  @Override
  public int getItemCount() {
    return gonderiler.size();
  }

  public static class MyViewHolder extends RecyclerView.ViewHolder {
    public CircleImageView imgProfil;
    public TextView txtAd, txtGosterim;
    public ImageView imgFoto;
    public LikeButton btnLike;
    public ImageButton btnMenu;
    public myOnClickListener onClickListener;

    public MyViewHolder(View itemView) {
      super(itemView);
      imgProfil = itemView.findViewById(R.id.card_imgprofil);
      txtAd = itemView.findViewById(R.id.card_txtUserName);
      txtGosterim = itemView.findViewById(R.id.card_txtGoruntulenme);
      imgFoto = itemView.findViewById(R.id.card_imgView);
      btnLike = itemView.findViewById(R.id.card_btnLike);
      btnMenu = itemView.findViewById(R.id.card_btnMenu);
      btnMenu.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          onClickListener.onItemClick(view, getPosition());
        }
      });
    }
  }

  public static class ViewHolderAdMob extends RecyclerView.ViewHolder {
    public AdView mAdView;

    public ViewHolderAdMob(View view) {
      super(view);
      mAdView = view.findViewById(R.id.ad_adView);
      AdRequest adRequest = new AdRequest.Builder()
        .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
        .build();
      mAdView.loadAd(adRequest);
    }
  }
}
