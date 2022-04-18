/*
 * Copyright (C) 2022 CARO Analytical Services
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package ca.caro.assetmanagement.entity;

import com.google.gson.annotations.SerializedName;


/**
 * The Entity class dedicated fo creating asset object to deal with "import from spreadsheet" function.
 * Contains the necessary getter and setter method to interact with the created object
 * @author Kin Shing Chong
 */
public class Asset_Entity_From_Import {
    private String asset_id;
    @SerializedName("Item") private String asset_category_description;
    @SerializedName("Description") private String asset_desc;
    @SerializedName("Vendor") private String vendor_name;
    @SerializedName("Brand")private String brand_name;
    @SerializedName("Model")private String asset_name;
    @SerializedName("SN")private String serial_number;
    @SerializedName("MAC")private String MAC;
    @SerializedName("Hostname")private String hostname;
    @SerializedName("Service Tag")private String service_tag;
    @SerializedName("Assigned To")private String assigned_to_email;
    @SerializedName("Physical Location")private String location_name;
    @SerializedName("OS")private String os;
    @SerializedName("Added By")private String added_by_email;
    @SerializedName("Purchase Date")private String purchase_date;
    @SerializedName("Cost")private double cost;
    @SerializedName("Comments")private String comment_on_asset;

    /**
     * Getter of asset_id
     * @return asset_id
     */
    public String getAsset_id() {
        return asset_id;
    }

    /**
     * Setter of asset_id
     * @param asset_id the asset_id 
     */
    public void setAsset_id(String asset_id) {
        this.asset_id = asset_id;
    }

    /**
     * Getter of asset_category_description
     * @return asset_category_description
     */
    public String getAsset_category_description() {
        return asset_category_description;
    }

    /**
     * Getter of asset_desc
     * @return asset_desc
     */
    public String getAsset_desc() {
        return asset_desc;
    }

    
    /**
     * Getter of vendor_name
     * @return vendor_name
     */
    public String getVendor_name() {
        return vendor_name;
    }

    
     /**
     * Getter of brand_name
     * @return brand_name
     */   
    public String getBrand_name() {
        return brand_name;
    }

     /**
     * Getter of asset_name
     * @return asset_name
     */       
    public String getAsset_name() {
        return asset_name;
    }

    
     /**
     * Getter of serial_number
     * @return serial_number
     */           
    public String getSerial_number() {
        return serial_number;
    }

    
     /**
     * Getter of MAC 
     * @return MAC
     */     
    public String getMAC() {
        return MAC;
    }

    
     /**
     * Getter of hostname
     * @return hostname
     */ 
    public String getHostname() {
        return hostname;
    }

    
     /**
     * Getter of service_tag
     * @return service_tag
     */     
    public String getService_tag() {
        return service_tag;
    }

    
     /**
     * Getter of assigned_to_email
     * @return assigned_to_email
     */     
    public String getAssigned_to_email() {
        return assigned_to_email;
    }

    
    /**
     * Getter of location_name
     * @return location_name
     */ 
    public String getLocation_name() {
        return location_name;
    }

    
    /**
     * Getter of os
     * @return os
     */ 
    public String getOs() {
        return os;
    }


     /**
     * Getter of added_by_email
     * @return added_by_email
     */     
    public String getAdded_by_email() {
        return added_by_email;
    }

    /**
     * Getter of purchase_date
     * @return purchase_date
     */ 
    public String getPurchase_date() {
        return purchase_date;
    }

     /**
     * Getter of cost
     * @return cost
     */     
    public double getCost() {
        return cost;
    }

    
     /**
     * Getter of comment_on_asset
     * @return comment_on_asset
     */     
    public String getComment_on_asset() {
        return comment_on_asset;
    }


        
}
