package br.com.uol.ps.beacon.others;

import android.os.Parcel;
import android.os.Parcelable;

import java.math.BigDecimal;

/**
 * @author Jean Rodrigo D. Cunha
 */
public class OffersModel implements Parcelable {

    private int cod;
    private String time;
    private String title;
    private BigDecimal value;
    private String imageResource;

    public OffersModel(int cod, String time, String title, BigDecimal value, String imageResource) {
        this.time = time;
        this.title = title;
        this.value = value;
        this.imageResource = imageResource;
        this.cod = cod;
    }

    protected OffersModel(Parcel in) {
        time = in.readString();
        title = in.readString();
        imageResource = in.readString();
        cod = in.readInt();
    }

    public static final Creator<OffersModel> CREATOR = new Creator<OffersModel>() {
        @Override
        public OffersModel createFromParcel(Parcel in) {
            return new OffersModel(in);
        }

        @Override
        public OffersModel[] newArray(int size) {
            return new OffersModel[size];
        }
    };

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public String getImageResource() {
        return imageResource;
    }

    public void setImageResource(String imageResource) {
        this.imageResource = imageResource;
    }

    public int getCod() {
        return cod;
    }

    public void setCod(int cod) {
        this.cod = cod;
    }

    @Override
    public String toString() {
        return "OffersModel{" +
                "cod=" + cod +
                ", time='" + time + '\'' +
                ", title='" + title + '\'' +
                ", value=" + value +
                ", imageResource=" + imageResource +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(time);
        parcel.writeString(title);
        parcel.writeString(imageResource);
        parcel.writeInt(cod);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        OffersModel that = (OffersModel) o;

        if (cod != that.cod) {
            return false;
        }
        if (time != null ? !time.equals(that.time) : that.time != null) {
            return false;
        }
        if (title != null ? !title.equals(that.title) : that.title != null) {
            return false;
        }
        if (value != null ? !value.equals(that.value) : that.value != null) {
            return false;
        }
        return !(imageResource != null ? !imageResource.equals(that.imageResource) : that.imageResource != null);

    }

    @Override
    public int hashCode() {
        int result = cod;
        result = 31 * result + (time != null ? time.hashCode() : 0);
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (value != null ? value.hashCode() : 0);
        result = 31 * result + (imageResource != null ? imageResource.hashCode() : 0);
        return result;
    }
}