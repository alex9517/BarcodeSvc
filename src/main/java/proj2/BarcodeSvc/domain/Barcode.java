//  Created : 2021-Jun-07
// Modified : 2021-Jun-09
//
////////////////////////
//
//    BARCODE ENTITY
//
////////////////////////

package proj2.BarcodeSvc.domain;

import java.util.Objects;
import java.io.Serializable;


public class Barcode implements Serializable {

    private Integer id;

    private String type;

    private String data;

    private byte[] picture;


    ////////////////
    //
    // CONSTRUCTORS
    //
    ///////////////////

    public Barcode() {
    }

    public Barcode( int id, String type, String data, byte[] picture ) {
        this.id = id > 0 ? id : 0;
        this.type = type;
        this.data = data;
        this.picture = picture;
    }


    /////////////
    //
    // GET / SET
    //
    /////////////////////////

    // NOTE!
    // The 'id' must be Integer (not primitive int),
    // and 'getId()' must return Integer, because int
    // sooner or later will cause problems!

    public Integer getId() {
        return id;
    }

    public void setId( int id ) {
        this.id = id > 0 ? id : 0;
    }

    public String getType() {
        return type;
    }

    public void setType( String type ) {
        this.type = type;
    }

    public String getData() {
        return data;
    }

    public void setData( String data ) {
        this.data = data;
    }

    public byte[] getPicture() {
        return picture;
    }

    public void setPicture( byte[] picture ) {
        this.picture = picture;
    }


    //////////
    //
    // EQUALS
    //
    /////////////////////////////////////////////

    @Override
    public boolean equals( Object otherObject ) {

        // Identity check;

        if ( this == otherObject ) return true;

        if (!( otherObject instanceof Barcode )) return false;

        Barcode that = ( Barcode ) otherObject;

        // State check;

        if ( !Objects.equals( this.id, that.id )) return false;
        if ( !Objects.equals( this.type, that.type )) return false;
        if ( !Objects.equals( this.data, that.data )) return false;
        if ( !Objects.equals( this.picture, that.picture )) return false;

        return true;
    }


    /////////////
    //
    // HASH CODE
    //
    ///////////////////////

    @Override
    public int hashCode() {
        return Objects.hash( id, type, data, picture );
    }

}

// -END-
