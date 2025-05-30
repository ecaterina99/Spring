package model;

import java.sql.*;

public class Impression {
    private int id;
    private String name;
    private String text;


    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public static String allImpressions() throws ClassNotFoundException {

        StringBuilder all_impressions = new StringBuilder();

        Class.forName("com.mysql.cj.jdbc.Driver");

        try(Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/users", "root", "123123")) {
            Statement st = conn.createStatement();
            st.executeQuery("select name, text from impressions");
            ResultSet rs = st.getResultSet();

            while(rs.next()) {
                all_impressions.append(rs.getString("name"));
                all_impressions.append(":\n");
                all_impressions.append(rs.getString("text"));
                all_impressions.append("\n\n");
            }
        } catch (SQLException ex) {
            all_impressions.append(ex.getMessage());
        }
        return all_impressions.toString();
    }

    public void insertImpression() throws ClassNotFoundException {

        Class.forName("com.mysql.cj.jdbc.Driver");

        try(Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/users", "root", "123123")) {
            if(name != null && !(name.isEmpty()) && text != null && !(text.isEmpty())) {
                Statement st = conn.createStatement();
                st.execute("insert into impressions (name, text) values ('" + name + "','" + text + "')");
            }
        } catch (SQLException ex) {
            System.out.println("Error in database connection: \n" + ex.getMessage());
        }
    }

}
