import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;

import javax.swing.*;
import javax.swing.event.*;


@SuppressWarnings("serial")
public class AddressBook extends JFrame implements ActionListener
{
    
    private final Color
    veryLightGrey = new Color(240, 240, 240),
    darkBlue = new Color(0, 0, 150),
    backGroundColour = veryLightGrey,
    navigationBarColour = Color.lightGray,
    textColour = darkBlue;
    private static final int
    windowWidth = 450, windowHeight = 600,               
    windowLocationX = 200, windowLocationY = 100;       
    private final int
    panelWidth = 450, panelHeight = 250,                 
    leftMargin = 50,                                     
    mainHeadingY = 30,                                   
    detailsY = mainHeadingY+40,                          
    detailsLineSep = 30;                                 
    private final Font
    mainHeadingFont = new Font("SansSerif", Font.BOLD, 20),
    detailsFont = new Font("SansSerif", Font.PLAIN, 14);

    /** The navigation buttons. */
    private JButton
    
    previous = new JButton("<"),          // For "move to previous contact" action
    next = new JButton(">");              // For "move to next contact" action
   

    /** The action buttons */
    private JButton
    addContact = new JButton("Add new contact"),   // To request adding a new contact
    deleteContact = new JButton("Delete contact"), // To delete the currently selected contact
    deleteAll = new JButton("Delete all"),         // To delete all contacts
    findContact = new JButton("Find exact name"),  // To find contact by exact match of name
    findPartial = new JButton("Find partial name");// To find contact by partial, case insensitive match of name
   

    /** Text fields for data entry for adding new contact and finding a contact */
    private JTextField
    nameField = new JTextField(20),                // For entering a new name, or a name to find
    addressField = new JTextField(30),             // For entering a new address
    mobileField = new JTextField(12),              // For entering a new mobile number
    emailField = new JTextField(30);               // For entering a new email address

    
    @SuppressWarnings("serial")
	private JPanel contactDetails = new JPanel()
        {
            
            public void paintComponent(Graphics g)
            {
                
                super.paintComponent(g); 
                paintScreen(g);          
            }
        };

    
    public static void main(String[] args)
    {
        AddressBook contacts = new AddressBook();
        contacts.setSize(windowWidth, windowHeight);
        contacts.setLocation(windowLocationX, windowLocationY);
        contacts.setTitle("My address book: 1234567");
        contacts.setUpAddressBook();
        contacts.setUpGUI();
        contacts.setVisible(true);
    } // End of main

    
    private void setUpAddressBook()
    {
        
        currentSize = 0;    // No contacts initially
        addContact("James", "12 Coronal Street, Mumbai", "07999232321", "james@cs.isp.com");
        addContact("Paul", "23 Battle Street, Delhi", "0033998877", "paul@paul.net");
        addContact("Gayle", "34 Corner Street, New Delhi", "01222 78160", "gayle@yahoo.com");
        addContact("Murali", "45 Path Street, Bangalore", "0999 8888", "murali@murali.net");
        addContact("Hardik", "Box street, Gurugram", "9512367415", "hardik@gmail.com");
        
        // currentSize should now be 5

        
        currentContact = 0;
    } // End of setUpAddressBook

    
    private void setUpGUI()
    {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        Container window = getContentPane();
        window.setLayout(new FlowLayout());
        window.setBackground(navigationBarColour);

        //  GUI buttons order
        
        //  previous (<), next (>)

        window.add(new JLabel("Navigation:"));
        window.add(previous);
        previous.addActionListener(this);
        window.add(next);
        next.addActionListener(this);
       
        // Set up graphics panel
        contactDetails.setPreferredSize(new Dimension(panelWidth, panelHeight));
        contactDetails.setBackground(backGroundColour);
        window.add(contactDetails);

        // Set up action buttons
        JPanel addDelPanel = new JPanel();
        addDelPanel.add(addContact);
        addContact.addActionListener(this);
        addDelPanel.add(deleteContact);
        deleteContact.addActionListener(this);
        addDelPanel.add(deleteAll);
        deleteAll.addActionListener(this);
        window.add(addDelPanel);
        JPanel findPanel = new JPanel();
        findPanel.add(findContact);
        findContact.addActionListener(this);
        findPanel.add(findPartial);
        findPartial.addActionListener(this);
        window.add(findPanel);
        

        
        JPanel namePanel = new JPanel();
        namePanel.add(new JLabel("New/find name:"));
        namePanel.add(nameField);
        window.add(namePanel);

        JPanel addressPanel = new JPanel();
        addressPanel.add(new JLabel("New address:"));
        addressPanel.add(addressField);
        window.add(addressPanel);

        JPanel mobilePanel = new JPanel();
        mobilePanel.add(new JLabel("New mobile:"));
        mobilePanel.add(mobileField);
        window.add(mobilePanel);

        JPanel emailPanel = new JPanel();
        emailPanel.add(new JLabel("New email:"));
        emailPanel.add(emailField);
        window.add(emailPanel);
    } // End of setUpGUI

    
    private void paintScreen(Graphics g)
    {
        
        g.setColor(textColour);
        g.setFont(mainHeadingFont);
        g.drawString("Contact details", leftMargin, mainHeadingY);

        
        displayCurrentDetails(g);
    } // End of paintScreen

    
    private void displayCurrentDetails(Graphics g)
    {
        g.setColor(textColour);
        g.setFont(detailsFont);
        if (currentContact == -1)           
            g.drawString("There are no contacts", leftMargin, detailsY);
        else
        {   
            g.drawString(name[currentContact], leftMargin, detailsY);
            g.drawString(address[currentContact], leftMargin, detailsY + detailsLineSep);
            g.drawString("Mobile: " + mobile[currentContact], leftMargin, detailsY + 2 * detailsLineSep);
            g.drawString("Email: " + email[currentContact], leftMargin, detailsY + 3 * detailsLineSep);
        }
    } // End of displayCurrentDetails

   
    public void actionPerformed(ActionEvent e)
    {
      

        // If previous is clicked: Cause the previous contact to become selected, if there is one
        if (e.getSource() == previous && currentContact > 0)
            currentContact--;

        // If next is clicked: Cause the next contact to become selected, if there is one
        if (e.getSource() == next && currentContact < currentSize - 1)
            currentContact++;

       

        // Add a new contact
        if (e.getSource() == addContact)
            doAddContact();

        // Delete the current contact
        if (e.getSource() == deleteContact)
            doDeleteContact();

        // Delete all contacts
        if (e.getSource() == deleteAll)
            doDeleteAll();

        // Find a contact with exact name match
        if (e.getSource() == findContact)
            doFindContact();

        // Find a contact with partial, case insensitive name match
        if (e.getSource() == findPartial)
            doFindPartial();

       

        
        repaint();
    } // End of actionPerformed

  
    private void doAddContact()
    {
        String newName = nameField.getText();       nameField.setText("");
        String newAddress = addressField.getText(); addressField.setText("");
        String newMobile = mobileField.getText();   mobileField.setText("");
        String newEmail = emailField.getText();     emailField.setText("");
        if (newName.length() == 0)         
        {
            JOptionPane.showMessageDialog(null, "No name entered");
            return;
        }
        int index = addContact(newName, newAddress, newMobile, newEmail); 
        if (index == -1)                   
            JOptionPane.showMessageDialog(null, "No space for new name");
        else
            currentContact = index;
            index++;        
    } // End of doAddContact

    
    private void doDeleteContact()
    {
        if (currentSize == 0)               
        {
            JOptionPane.showMessageDialog(null, "No contacts to delete");
            return;
        }
        deleteContact(currentContact);
        
        if (currentContact == currentSize)    
            currentContact--;                 
    } // End of doDeleteContact

    
    private void doDeleteAll()
    {
        clearContacts();
        currentContact = -1;    
    } // End of doDeleteAll

    
    private void doFindContact()
    {
        String searchName = nameField.getText();
        if (searchName.length() == 0)               
        {
            JOptionPane.showMessageDialog(null, "Name must not be empty");
            return;
        }
        int location = findContact(searchName);     
        if (location == -1)                         
            JOptionPane.showMessageDialog(null, "Name not found");
        else
        {
            currentContact = location;              
            nameField.setText("");                  
        }
    } // End of doFindContact

   
    private void doFindPartial()
    {
        String searchText = nameField.getText();
        if (searchText.length() == 0)               
        {
            JOptionPane.showMessageDialog(null, "Search text must not be empty");
            return;
        }
        int location = findPartial(searchText);     
        if (location == -1)                         
            JOptionPane.showMessageDialog(null, "Name not found");
        else
        {
            currentContact = location;              
            nameField.setText("");                  
        }
    } // End of doFindPartial

    
    
    private final int databaseSize = 10;

    
    private String[]
    name = new String[databaseSize],
    address = new String[databaseSize],
    mobile = new String[databaseSize],
    email = new String[databaseSize];

   
    private int currentSize = 0;

    
    private int currentContact = -1;

    private int addContact(String newName, String newAddress, String newMobile, String newEmail)
    {   
        name[currentSize] = newName;        
        address[currentSize] = newAddress;
        mobile[currentSize] = newMobile;
        email[currentSize] = newEmail;
        currentSize++;                       
        return currentSize-1;                

    }
    // End of addContact

    
    private void deleteContact(int index)
    {
        
    	if (index > -1)
    	{   
            for (int i = index; i < currentSize; i++)
            {
            	name[i] = name[i+1];         
                address[i] = address[i+1];
                mobile[i] = mobile[i+1];
                email[i] = email[i+1];
            }
            
            currentSize--;

    	} else return;
    } // End of deleteContact

    
    private void clearContacts()
    {	
    	for (int i = 0; i < currentSize; i++)
    	{
    		name[i] = null;         
            address[i] = null;
            mobile[i] = null;
            email[i] = null;
            
            currentSize--;
    	}
    } // End of clearContacts

    
    private int findContact(String searchName)
    {   
    	for (int i = 0; i < currentSize; i++)
    	{
    		if (name[i].equals(searchName))
    			return i;
    	}
    	
        return -1;                          
    } // End of findContact

   
    private int findPartial(String searchText)
    {   
    	for (int i = 0; i < currentSize; i++)
    	{
    		if (name[i].contains(searchText))
    			return i;
    	}
        return -1;                          
    } // End of findPartial

   

} // End of AddressBook

