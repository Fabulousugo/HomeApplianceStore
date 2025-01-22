// Code written and maintained by
// Ugochukwu Precious Onah
// 24848777
package org.mjb.dao;

import org.mjb.Connector;
import org.mjb.systemModels.Appliance;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;

/**
 * The type Appliance or product dao.
 * For brevity, the Appliance is also called the product.
 * It is used interchangeably throughout the class and project as a whole.
 */
public class ApplianceDAO {
    /**
     * Find Appliances array list.
     *
     * @param search the search
     * @return the array list
     */
    public ArrayList<Appliance> findAppliances(String search){
        ArrayList<Appliance> products = new ArrayList<Appliance>();
        try{
            Connection connection = new Connector().connect();
            ResultSet result = connection.createStatement().executeQuery(String.format("SELECT * FROM products %s",search != null && !search.trim().isEmpty() ? ("WHERE description LIKE '%"+search+"%' ") : ""));
            while(result.next()){
                products.add(new Appliance(result.getString("description"),result.getString("category"),result.getString("sku"),result.getInt("stock"),result.getInt("price"),result.getString("expiresOn"),result.getInt("id")));
            }
            connection.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        return products;
    }

    /**
     * Find expired Appliances array list.
     *
     * @param search the search
     * @return the array list
     */
    public ArrayList<Appliance> findExpiredAppliances(String search){
        ArrayList<Appliance> products = new ArrayList<Appliance>();
        try{
            Connection connection = new Connector().connect();
            ResultSet result = connection.createStatement().executeQuery(String.format("SELECT * FROM products WHERE expiresOn < DATE('now') %s",search != null && !search.trim().isEmpty() ? ("AND description LIKE '%"+search+"%' ") : ""));
            while(result.next()){
                products.add(new Appliance(result.getString("description"),result.getString("category"),result.getString("sku"),result.getInt("stock"),result.getInt("price"),result.getString("expiresOn"),result.getInt("id")));
            }
            connection.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        return products;
    }

    /**
     * Find Appliance.
     *
     * @param productId //
     * @return the Appliance
     */
    public Appliance findAppliance(int productId){
        Appliance product = null;
        try{
            Connection connection = new Connector().connect();
            ResultSet result = connection.createStatement().executeQuery(String.format("SELECT * FROM products WHERE id = %s",productId));
            while(result.next()){
                product = new Appliance(result.getString("description"), result.getString("category"), result.getString("sku"), result.getInt("stock"), result.getInt("price"), result.getString("expiresOn"), result.getInt("id"));
            }
            connection.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        return product;
    }

    /**
     * Create Appliances boolean.
     *
     * @param product the product
     * @return the boolean
     */
    public boolean createAppliance(Appliance product){
        try{
            Connection connection = new Connector().connect();
            connection.createStatement().execute(String.format("INSERT INTO products (category,description,sku,price,stock,expiresOn) VALUES ('%s','%s','%s','%s','%s','%s')",
                    product.getCategory(),product.getDescription(),product.getSku(),product.getPrice(),product.getStock(),product.getExpiresOn()
                )
            );
            connection.close();
            return true;
        }catch(Exception e){
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Update Appliance boolean.
     *
     * @param product the product
     * @return the boolean
     */
    public boolean updateAppliance(Appliance product){
        try{
            Connection connection = new Connector().connect();
            connection.createStatement().execute(String.format("UPDATE products SET category = '%s',description = '%s',sku = '%s',price = '%s',stock = '%s',expiresOn = '%s' WHERE id = %s",
                    product.getCategory(),product.getDescription(),product.getSku(),product.getPrice(),product.getStock(),product.getExpiresOn(),product.getId()
                )
            );
            connection.close();
            return true;
        }catch(Exception e){
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Delete Appliance boolean.
     *
     * @param productId the product id
     * @return the boolean
     */
    public boolean deleteProduct(int productId){
        try{
            Connection connection = new Connector().connect();
            connection.createStatement().execute(String.format("DELETE FROM products WHERE id = %s", productId));
            connection.close();
            return true;
        }catch(Exception e){
            e.printStackTrace();
        }
        return false;
    }
}
