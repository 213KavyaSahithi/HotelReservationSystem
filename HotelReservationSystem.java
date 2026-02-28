package Reservation;

import java.sql.*;
import java.util.*;
public class HotelReservationSystem {
	static class Pair{
		boolean isSuccess;
		ArrayList<String> entry;
		Pair(boolean isSuccess,ArrayList<String> entry){
			this.isSuccess=isSuccess;
			this.entry=entry;
		}
	}
	public static void main(String args[]) throws ClassNotFoundException,SQLException{
		Scanner input=new Scanner(System.in);
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		}catch(ClassNotFoundException e) {
			System.out.println(e.getMessage());
		}
		String url="jdbc:mysql://localhost:3306/hotel_db";
		String username="root";
		String password="your_password_here";
		try {
			Connection con=DriverManager.getConnection(url,username,password);
			System.out.println("Welcome to Hotel Reservation System");
			while(true) {
				System.out.println();
				System.out.println("1. Create Room Reservation");
				System.out.println("2. View your Reservation Details");
				System.out.println("3. Get your RoomNumber");
				System.out.println("4. Update your details in the Reservation");
				System.out.println("5. Delete your Reservation");
				System.out.println("6. View all Reservations (Only for Admin)");
				System.out.println("7. Exit");
				System.out.println("Enter your input:");
				int n=input.nextInt();
				switch(n) {
				case 1:
					createReservation(input,con);
					break;
				case 2:
					viewReservationDetails(input,con);
					break;
				case 3:
					getRoomNumber(input,con);
					break;
				case 4:
					updateReservation(input,con);
					break;
				case 5:
					deleteReservation(input,con);
					break;
				case 6:
					viewAllReservations(con);
					break;
				case 7:
					exit(con);
					return;
				default:
					System.out.println("Invalid Input. Try Again...");
				}
			}
		}catch(SQLException e) {
			System.out.println(e.getMessage());
		}
	}
	public static void createReservation(Scanner input,Connection con) {
		input.nextLine();
		System.out.println("Enter guest name:");
		String name=input.nextLine();
		System.out.println("Enter room number:");
		int num=input.nextInt();
		System.out.println("Enter contact number:");
		input.nextLine();
		String contact=input.nextLine();
		try {
			Statement stmt=con.createStatement();
			String query="insert into hotel_reservations(guest_name,room_number,contact_number) values('"+
							name+"',"+num+",'"+contact+"')";
			int rows=stmt.executeUpdate(query,Statement.RETURN_GENERATED_KEYS);
			if(rows>0) {
				ResultSet rs=stmt.getGeneratedKeys();
	            if(rs.next()){
	                int generatedId = rs.getInt(1);
	                System.out.println("Reservation Successful!");
	                System.out.println("Your Reservation ID is: " + generatedId);
	                System.out.println("Note your reservation id for future references.");
	            }
			}
			else
				System.out.println("Reservation Failed");
		}catch(SQLException e) {
			System.out.println(e.getMessage());
		}
	}
	public static void viewReservationDetails(Scanner input,Connection con) {
		System.out.println("Enter your Reservation Id to view details:");
		int id=input.nextInt();
		Pair p=isReserved(con,id);
		if(p.isSuccess) {
			System.out.println("Reservation Found");
			ArrayList<String> arr=p.entry;
			System.out.println("Reservation Id:   "+arr.get(0));
			System.out.println("Guest Name:       "+arr.get(1));
			System.out.println("Room Number:      "+arr.get(2));
			System.out.println("Contact Number:   "+arr.get(3));
			System.out.println("Reservation Date: "+arr.get(4));
		}else {
			System.out.println("Reservation not found!");
			System.out.println("Try with correct Reservation Id");
		}
	}
	public static Pair isReserved(Connection con,int id) {
		try {
			Statement stmt=con.createStatement();
			String query="select * from hotel_reservations";
			ResultSet rs=stmt.executeQuery(query);
			boolean found=false;
			ArrayList<String> ar=new ArrayList<>();
			while(rs.next()) {
				int r_id=rs.getInt("reservation_id");
				if(r_id==id) {
					found=true;
					ar.add(String.valueOf(r_id));
					ar.add(rs.getString("guest_name"));
					ar.add(String.valueOf(rs.getInt("room_number")));
					ar.add(rs.getString("contact_number"));
					ar.add(rs.getTimestamp("reservation_date").toString());
					break;
				}
			}
			return new Pair(found,ar);
		}catch(SQLException e) {
			System.out.println(e.getMessage());
			return new Pair(false,new ArrayList<>());
		}
	}
	public static void getRoomNumber(Scanner input,Connection con) {
		System.out.println("Enter reservation Id to know room number:");
		int id=input.nextInt();
		Pair p=isReserved(con,id);
		if(p.isSuccess) {
			System.out.println("Reservation Found");
			System.out.println("Room Number with reservation id "+id+" is: "+p.entry.get(2));
		}else {
			System.out.println("Reservation not found!");
			System.out.println("Try with correct Reservation Id");
		}
	}
	public static void updateReservation(Scanner input,Connection con) {
		try {
			Statement stmt=con.createStatement();
			System.out.println("Enter reservation Id to update:");
			int id=input.nextInt();
			input.nextLine();
			Pair p=isReserved(con,id);
			if(p.isSuccess) {
				System.out.println("Reservation Found");
				System.out.println("Enter the new value to update else leave blank and click enter");
				System.out.println("Enter new guest name:");
				String newName=input.nextLine();
				if(newName.strip().length()==0)
					newName=p.entry.get(1);
				System.out.println("Enter new Room Number:");
				String newRoomStr=input.nextLine();
				int newRoom;
				if(newRoomStr.strip().length()==0)
					newRoom=Integer.parseInt(p.entry.get(2));
				else
					newRoom=Integer.parseInt(newRoomStr);
				System.out.println("Enter new contact number:");
				String newNum=input.nextLine();
				if(newNum.strip().length()==0)
					newNum=p.entry.get(3);
				String query="update hotel_reservations set guest_name='"+newName+"', room_number="+newRoom+",contact_number='"+newNum+"' where reservation_id="+id;
				int numRows=stmt.executeUpdate(query);
				if(numRows>0)
					System.out.println("Reservation updated successfully");
				else {
					System.out.println("Unable to update reservation");
					System.out.println("Try Again!");
				}
				
			}else {
				System.out.println("Reservation not found!");
				System.out.println("Try with correct Reservation Id");
			}
		}catch(SQLException e) {
			System.out.println(e.getMessage());
		}
	}
	public static void deleteReservation(Scanner input,Connection con) {
		try {
			Statement stmt=con.createStatement();
			System.out.println("Enter reservation Id to delete:");
			int id=input.nextInt();
			Pair p=isReserved(con,id);
			if(p.isSuccess) {
				System.out.println("Reservation Found");
				System.out.println("Deleting your Reservation...");
				String query="delete from hotel_reservations where reservation_id="+id;
				int rows=stmt.executeUpdate(query);
				if(rows>0) {
					System.out.println("Reservation deleted successfully!");
				}else {
					System.out.println("Failed to delete reservation");
					System.out.println("Try Again!");
				}
			}else {
				System.out.println("Reservation not found!");
				System.out.println("Try with correct Reservation Id");
			}
		}catch(SQLException e) {
			System.out.println(e.getMessage());
		}
	}
	public static void viewAllReservations(Connection con) {
		try {
			Statement stmt=con.createStatement();
			String query="select * from hotel_reservations";
			ResultSet rs=stmt.executeQuery(query);
			 System.out.println("Current Reservations:");
	         System.out.println("+----------------+----------------------+---------------+----------------------+-------------------------+");
	         System.out.println("| Reservation_ID | Guest Name           | Room Number   | Contact Number       | Reservation Date        |");
	         System.out.println("+----------------+----------------------+---------------+----------------------+-------------------------+");
			while(rs.next()) {
				int id=rs.getInt("reservation_id");
				String name=rs.getString("guest_name");
				int room=rs.getInt("room_number");
				String contact=rs.getString("contact_number");
				String date=rs.getTimestamp("reservation_date").toString();
				System.out.printf("| %-14d | %-20s | %-13d | %-20s | %-19s   |\n",
                        id, name, room, contact, date);
			}
			System.out.println("+----------------+----------------------+---------------+----------------------+-------------------------+");
		}catch(SQLException e) {
			System.out.println(e.getMessage());
		}
	}
	public static void exit(Connection con) {
		try {
			con.close();
			System.out.print("Exiting");
			for(int i=0;i<5;i++) {
				System.out.print(".");
				Thread.sleep(1000);
			}
			System.out.println();
			System.out.println("ThankYou for using Hotel Reservation System");
		}catch(SQLException e) {
			System.out.println(e.getMessage());
		}catch(InterruptedException e) {
			System.out.println(e.getMessage());
		}
	}
}

