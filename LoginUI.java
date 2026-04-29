import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class LoginUI {
    public static int userId = -1;

    public LoginUI() {
        JFrame f = new JFrame("Login");
        f.setSize(300,200);
        f.setLayout(new GridLayout(3,2));

        JTextField user = new JTextField();
        JPasswordField pass = new JPasswordField();

        JButton login = new JButton("Login");
        JButton register = new JButton("Register");

        f.add(new JLabel("Username"));
        f.add(user);
        f.add(new JLabel("Password"));
        f.add(pass);
        f.add(login);
        f.add(register);

        login.addActionListener(e -> {
            try {
                Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(
                        "SELECT * FROM users WHERE username=? AND password=?"
                );
                ps.setString(1, user.getText());
                ps.setString(2, new String(pass.getPassword()));

                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    userId = rs.getInt("id");
                    f.dispose();
                    SmartCampus.launchApp();
                } else {
                    JOptionPane.showMessageDialog(f, "Invalid login");
                }
            } catch(Exception ex){ ex.printStackTrace(); }
        });

        register.addActionListener(e -> {
            try {
                Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(
                        "INSERT INTO users(username,password) VALUES(?,?)"
                );
                ps.setString(1, user.getText());
                ps.setString(2, new String(pass.getPassword()));
                ps.executeUpdate();

                JOptionPane.showMessageDialog(f, "Registered!");
            } catch(Exception ex){ ex.printStackTrace(); }
        });

        f.setVisible(true);
    }
}
