package fishfarm.gotech;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by aruna.ramakrishnan on 3/7/2018.
 */

public class FeedTypeModel  {

    private String id;
    private String name;
    private String qty;

    public FeedTypeModel(String id, String name, String qty, String phone1, String phone2, String jobHdId) {
        this.id = id;
        this.name = name;
        this.qty = qty;
    }

    public FeedTypeModel(String name, String qty) {
        this.name = name;
        this.qty = qty;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }



    public FeedTypeModel(Parcel source) {
        id = source.readString();
        name = source.readString();
        qty = source.readString();
    }

    public static final Parcelable.Creator<FeedTypeModel> CREATOR = new Parcelable.Creator<FeedTypeModel>() {

        @Override
        public FeedTypeModel createFromParcel(Parcel source) {
            return new FeedTypeModel(source);
        }

        @Override
        public FeedTypeModel[] newArray(int size) {
            return new FeedTypeModel[size];
        }
    };
}
