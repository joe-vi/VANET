//package vanet;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import java.util.*;


class vehicle{
	int vehicle_id;
	int event;
	int wait_time;
	double vehicle_pos_x;
	double vehicle_pos_y;
	float vehicle_speed;


	int get_vehicle_id(){
		return vehicle_id;
	}
	double get_vehicle_pos_x(){
		return vehicle_pos_x;
	}
	double get_vehicle_pos_y(){
		return vehicle_pos_y;
	}
	float get_vehicle_speed(){
		return vehicle_speed;
	}
	int get_wait_time() {
		return wait_time;
	}

	int get_event() {
		return event;
	}
	void set_vehicle_id(int id){
		vehicle_id = id;
	}
	void set_vehicle_pos_x(double x){
		vehicle_pos_x = x;
	}
	void set_vehicle_pos_y(double y){
		vehicle_pos_y = y;
	}
	void set_vehicle_speed(float speed){
		vehicle_speed = speed;
	}
	void set_wait_time(int time) {
		wait_time=time;
	}
	void random_event(int x) {
		event=x;
	}
}



class packet{
	int pack_id;
	int veh_id;
	int orig_second;
	ArrayList<Integer> group_num = new ArrayList<Integer>();
	int event;
	boolean msg_sent = false;
	int time_to_live;
	int tries = 0;

	int delay = 0;

	public void set_delay(int del) {
		delay = delay+ del;
	}

	public int get_pack_id(){
		return pack_id;
	}
	public void set_pack_id(int id){
		pack_id = id;
	}
	public int get_delay() {
		return delay;
	}

	//static int sent_count=0;

	public int get_vehicle_id(){
		return veh_id;
	}
	public int get_orig_sec(){
		return orig_second;
	}
	public ArrayList<Integer> get_group_num(){
		return group_num;
	}
	public int get_event(){
		return event;
	}
	public int get_time_to_live(){
		return time_to_live;
	}
	public void set_vehicle_id(int id){
		veh_id = id;
	}
	public void set_event(int evnt){
		event = evnt;
	}
	public void set_orig_sec(int sec){
		orig_second = sec;
	}
	public void set_group_num(int gnum){
		group_num.add(gnum);
	}
	public void set_msg_sent(){
		msg_sent = true;
	}
	public void set_time_to_live(int time){
		time_to_live = time;
	}
	public void dec_time_to_live(){
		time_to_live--;
	}
	public int get_tries(){
		return tries;
	}
	public int set_tries(){
		tries++;
		if(tries == 3){
			return -1;
		}
		else{
			return 1;
		}
	}
	public void set_tries(int num){
		tries = num;
	}
}


public class XmlRead {

	static int[] event_byte_size = {100,200,300,400};

	static int[] difs = {2,3,6,9};



	static double t_put = 0;
	static double delay = 0;

	static int total_number_groups = 0;
	static double pckt_dlvr_ratio = 0;
	static double bytes_delivered = 0;
	static double total_pckts = 0;
	static double sent_pckts = 0;
	static double drop_pckts = 0;
	static double total_delay = 0;

	static double time_st = 0;

	static int episodes = 1000;
	static int total_number_of_vehicles = 100;
	static int simulation_time = 500;
	static double range_radius = 200.0;


	public static void simulation_reset() {
		 t_put = 0;
		 pckt_dlvr_ratio = 0;
		 bytes_delivered = 0;
		 total_pckts = 0;
		 sent_pckts = 0;
		 drop_pckts = 0;
		 //total_number_groups = 0;
		 total_delay = 0;
		 delay = 0;
		 //full_details.clear();
		// avg_group = 0;
		// avg_thruput = 0;
		// avg_ratio = 0;
		// avg_delay = 0;

	}
	public static void episode_reset() {
		episode_tot_delay=0;
		episode_tot_thruput=0;
		episode_tot_dlvr_ratio=0;
		episode_tot_group=0;
		total_number_groups = 0;
		full_details.clear();
		//temp_list.clear();
	}

	static ArrayList<ArrayList<vehicle>> full_details = new ArrayList<ArrayList<vehicle>>();
	static ArrayList<vehicle> temp_list = new ArrayList<vehicle>();
	static double episode_tot_delay=0;
	static double episode_tot_thruput=0;
	static double episode_tot_dlvr_ratio=0;
	static double episode_tot_group=0;
	static double avg_group = 0;
	static double avg_thruput = 0;
	static double avg_ratio = 0;
	static double avg_delay = 0;





	public static void main(String args[]) {
		try {
			FileWriter fileWriter = new FileWriter("output.csv",false);
	  	fileWriter.append("No.of cars,groups,throughput,delivery ratio,delay\n");
		  fileWriter.close();
		}
		catch(Exception e) {

		}
		System.out.println("\n[*]Running Simulation");
		System.out.println("------------------------------------------------------------");
		System.out.println();

		for(;total_number_of_vehicles<=200;total_number_of_vehicles+=10) {
			episode_reset();
			//System.out.println(full_details.size());
			try {
				File file = new File("trace.xml");
				DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
				Document document = documentBuilder.parse(file);
				if (document.hasChildNodes())   {
					printNodeList(document.getChildNodes());
				}
			}
			catch (Exception e) {
				System.out.println(e.getMessage());
			}
			int i;
			System.out.println(full_details.size());
			total_number_groups = 0;
			ArrayList<ArrayList<ArrayList<vehicle>>> seconds_groups = new ArrayList<ArrayList<ArrayList<vehicle>>>();
			for(i = 0;i<simulation_time;i++){
				if(i>0){
					ArrayList<ArrayList<vehicle>> groups = groups_per_second(i-1);
					seconds_groups.add(groups_per_second(groups,i));
				}
				else{
					seconds_groups.add(groups_per_second(i));
				}
			}
			//System.out.println("NUmber of vehicles: " + total_number_of_vehicles);
			//System.out.println(total_number_groups);


			for(int q=0;q<episodes;q++) {
				System.out.println("[+]Running Simulation : "+(q+1));
				simulation_reset();
				set_random_event();
				network_implement(seconds_groups);
				t_put = sent_pckts/total_delay;
				pckt_dlvr_ratio = sent_pckts/total_pckts;
				delay = total_delay/sent_pckts;
				episode_tot_delay+=delay;
				episode_tot_thruput+=t_put;
				episode_tot_dlvr_ratio+=pckt_dlvr_ratio;
				System.out.println("\n\n\tEpisode STATS");
				System.out.println("\n\t Total number of pakcets: "+total_pckts);
				System.out.println("\n\t Total number of DROPPED pakcets: "+drop_pckts);
				System.out.println("\n\t Total number of SENT pakcets: "+sent_pckts);
				//System.out.println("\n\t Total bytes delivered : "+bytes_delivered);
				System.out.println("\n**************************************");
			}


			avg_thruput =(episode_tot_thruput/episodes);
			avg_ratio =(episode_tot_dlvr_ratio/episodes);
			avg_delay =(episode_tot_delay/episodes);
			System.out.println("\n\t Total number of GROUPS: "+total_number_groups);
			System.out.println("\n\t Throughput : "+avg_thruput+" Packets/sec");
			System.out.println("\n\t Packet Delivery Ratio : "+avg_ratio);
			System.out.println("\n\t Delay : "+avg_delay+" sec");
			try {
				WriteToFile(total_number_of_vehicles,total_number_groups,avg_thruput,avg_ratio,avg_delay);
			}
			catch(Exception e) {
				System.out.println(e.getMessage());

			}
		}


	}

	public static void WriteToFile(int a,double w,double x,double y,double z) throws IOException {
		String fileContent = a+","+w+","+x+","+y+","+z;
		FileWriter fileWriter = new FileWriter("output.csv",true);
	    fileWriter.append(fileContent);
	    fileWriter.append("\n");
	    //fileWriter.write(fileContent);
	    fileWriter.close();
	}




	public static double distance(double x1, double y1, double x2, double y2) {
		double distance=0.0;
		distance=Math.sqrt((x2-x1)*(x2-x1) + (y2-y1)*(y2-y1));
		return distance;
	}



	public static  void set_random_event() {
		int min = 1;
		int range = 4;
		int rand=5;
		int size = simulation_time;
		int range2 = 10;
		for(int i=0;i<size;i++) {
			ArrayList<vehicle> vehicle2 = full_details.get(i);
			int size2 = vehicle2.size();
			for(int k=0;k<size2;k++) {
				vehicle car = vehicle2.get(k);
				rand = (int)(Math.random() * range) + min;
				car.random_event(rand);
				int wait_time=0;
				if(rand == 1) {
					wait_time=difs[0]+((int)(Math.random() * range2) + min);
				}
				else if (rand == 2) {
					wait_time=difs[1]+((int)(Math.random() * range2) + min);
				}
				else if(rand == 3) {
					wait_time=difs[2]+((int)(Math.random() * range2) + min);
				}
				else if(rand == 4) {
					wait_time=difs[3]+((int)(Math.random() * range2) + min);
				}
				car.set_wait_time(wait_time);
			}
		}
	}



	private static void printNodeList(NodeList nodeList){
		int time = 0;
		for (int count = 0; count < nodeList.getLength(); count++){
			Node elemNode = nodeList.item(count);
			if (elemNode.getNodeType() == Node.ELEMENT_NODE){
				// get node name and value
				vehicle temp_vehicle = new vehicle();
				if (elemNode.hasAttributes()){
					NamedNodeMap nodeMap = elemNode.getAttributes();
					for (int i = 0; i < nodeMap.getLength(); i++){

						Node node = nodeMap.item(i);

						if(node.getNodeName().equalsIgnoreCase("id")){
							if(node.getNodeValue().length()<5){
								temp_vehicle.set_vehicle_id(Integer.parseInt(String.valueOf(node.getNodeValue().charAt(3))));
							}
							else if(node.getNodeValue().length()<6){
								int a = Integer.parseInt(String.valueOf(node.getNodeValue().charAt(3))) * 10;
								temp_vehicle.set_vehicle_id(a + Integer.parseInt(String.valueOf(node.getNodeValue().charAt(4))));
							}
							else if(node.getNodeValue().length()<7){
								int a = Integer.parseInt(String.valueOf(node.getNodeValue().charAt(3))) * 10;
								a = (a+Integer.parseInt(String.valueOf(node.getNodeValue().charAt(4)))) * 10;
								temp_vehicle.set_vehicle_id(a + Integer.parseInt(String.valueOf(node.getNodeValue().charAt(5))));
							}
						}
						if(node.getNodeName().equalsIgnoreCase("x")){
							temp_vehicle.set_vehicle_pos_x(Double.parseDouble(node.getNodeValue()));
						}
						if(node.getNodeName().equalsIgnoreCase("y")){
							temp_vehicle.set_vehicle_pos_y(Double.parseDouble(node.getNodeValue()));
						}
						if(node.getNodeName().equalsIgnoreCase("speed")){
							temp_vehicle.set_vehicle_speed(Float.parseFloat(node.getNodeValue()));
						}
					}
				}
				if (elemNode.hasChildNodes()){
					//recursive call if the node has child nodes
					printNodeList(elemNode.getChildNodes());
				}
				if(elemNode.getNodeName().equalsIgnoreCase("vehicle")){

					if(temp_vehicle.get_vehicle_id() < total_number_of_vehicles){
						temp_list.add(temp_vehicle);
					}
					temp_vehicle = new vehicle();
				}
				if(elemNode.getNodeName().equalsIgnoreCase("timestep")){
					time++;
					full_details.add(temp_list);
					temp_list = new ArrayList<vehicle>();
					if(time>=simulation_time){
						break;
					}
				}
			}
		}
	}

	static int check_for_repeat(ArrayList<ArrayList<vehicle>> main_list, ArrayList<vehicle> check_list){
		int i,j;
		int return_value=-1;
		ArrayList<Integer> check_list_ids = new ArrayList<Integer>();
		ArrayList<Integer> main_list_ids = new ArrayList<Integer>();

		for(i = 0; i < check_list.size();i++){
			vehicle veh = new vehicle();
			veh = check_list.get(i);
			check_list_ids.add(veh.get_vehicle_id());
		}
		Collections.sort(check_list_ids);
		for(i=0;i<main_list.size();i++){
			main_list_ids.clear();
			ArrayList<vehicle> main_list_group = main_list.get(i);
			if(main_list_group.size() == check_list.size()){
				for(j=0;j < main_list_group.size();j++){
					vehicle veh = new vehicle();
					veh = main_list_group.get(j);
					main_list_ids.add(veh.get_vehicle_id());
				}
				Collections.sort(main_list_ids);
				if(check_list_ids.equals(main_list_ids)){
					return_value = i;
					break;
				}
			}
		}
		return return_value;
	}




	static ArrayList<ArrayList<vehicle>> groups_per_second(int req_second){
		//transmit();
		ArrayList<vehicle> list_vehicles_second = new ArrayList<vehicle>();
		vehicle car = new vehicle();

		ArrayList<ArrayList<vehicle>> result_groups = new ArrayList<ArrayList<vehicle>>();
		//ArrayList<vehicle> group_of_vehicle = new ArrayList<vehicle>();


		double current_pos_x, current_pos_y;
		//range_radius is used for setting the maximum range possible
		//	double range_radius = 300.0;
		double distance;
		int i,j;
		int count_vehicles_timestamp;
		list_vehicles_second = full_details.get(req_second);
		count_vehicles_timestamp = list_vehicles_second.size();
		for(i=0;i<count_vehicles_timestamp;i++){
			ArrayList<vehicle> temp_group_of_vehicle = new ArrayList<vehicle>();
			car = list_vehicles_second.get(i);
			temp_group_of_vehicle.add(car);
			current_pos_x = car.get_vehicle_pos_x();
			current_pos_y = car.get_vehicle_pos_y();
			for(j=0;j<count_vehicles_timestamp;j++){
				if(j != i){
					car = list_vehicles_second.get(j);
					double x,y;
					x = current_pos_x-car.get_vehicle_pos_x();
					y = current_pos_y-car.get_vehicle_pos_y();
					distance = Math.sqrt(x*x + y*y);
					if(distance < range_radius){
						temp_group_of_vehicle.add(car);
					}
				}
			}
			if(check_for_repeat(result_groups,temp_group_of_vehicle) == -1){
				result_groups.add(temp_group_of_vehicle);
			}
		}
		return result_groups;
	}




	static ArrayList<ArrayList<vehicle>> groups_per_second(ArrayList<ArrayList<vehicle>> main_list,int req_second){
		ArrayList<vehicle> list_vehicles_second = new ArrayList<vehicle>();
		vehicle car = new vehicle();
		ArrayList<ArrayList<vehicle>> result_groups = new ArrayList<ArrayList<vehicle>>();
		//what is the need of group_of_vehicle
		ArrayList<vehicle> group_of_vehicle = new ArrayList<vehicle>();
		double current_pos_x, current_pos_y;
		double distance;
		int i,j,k;
		int count_vehicles_timestamp;
		//double range_radius = 300.0;
		list_vehicles_second = full_details.get(req_second);
		count_vehicles_timestamp = list_vehicles_second.size();

		for(i=0;i<count_vehicles_timestamp;i++){
			ArrayList<vehicle> temp_group_of_vehicle = new ArrayList<vehicle>();
			car = list_vehicles_second.get(i);
			temp_group_of_vehicle.add(car);
			current_pos_x = car.get_vehicle_pos_x();
			current_pos_y = car.get_vehicle_pos_y();
			for(j=0;j<count_vehicles_timestamp;j++){
				if(j != i){
					car = list_vehicles_second.get(j);
					double x,y;
					x = current_pos_x-car.get_vehicle_pos_x();
					y = current_pos_y-car.get_vehicle_pos_y();
					distance = Math.sqrt(x*x + y*y);
					if(distance < range_radius && distance != 0){
						temp_group_of_vehicle.add(car);
					}
				}
			}

			if(check_for_repeat(result_groups,temp_group_of_vehicle) == -1){

				//what is the need for this loop
				for(j=0;j<temp_group_of_vehicle.size();j++){
					vehicle veh= temp_group_of_vehicle.get(j);
				}
				int tttt = check_for_repeat(main_list,temp_group_of_vehicle);
				if(tttt == -1){
					result_groups.add(temp_group_of_vehicle);
					total_number_groups++;
				}
				else{
					result_groups.add(main_list.get(tttt));
				}
			}
		}
		return result_groups;
	}

	static int check_if_present(int id, ArrayList<vehicle> alist){
		int i;
		for(i=0;i<alist.size();i++){
			vehicle car = alist.get(i);
			if(id == car.get_vehicle_id()){
				return i;
			}
		}
		return -1;
	}


	static void calc_t_put(packet p) {
		if(p.get_event()==difs[0]) {
			bytes_delivered=bytes_delivered+event_byte_size[0];
		}
		else if(p.get_event()==difs[1]) {
			bytes_delivered=bytes_delivered+event_byte_size[1];

		}
		else if(p.get_event()==difs[2]) {
			bytes_delivered=bytes_delivered+event_byte_size[2];

		}
		else if(p.get_event()==difs[3]) {
			bytes_delivered=bytes_delivered+event_byte_size[3];

		}

	}


	static void check_to_send(ArrayList<ArrayList<ArrayList<vehicle>>> main_list, ArrayList<packet> que, int time){
		int i,j,k;
		//ArrayList<Integer> pos = new ArrayList<Integer>();
		int num_packet_to_send = 0;
		int num_groups_present = 0;
		ArrayList<ArrayList<vehicle>> list = new ArrayList<ArrayList<vehicle>>();

		packet p = que.get(0);
		i=0;
		while(que.get(i).get_time_to_live() == 0){
			if(check_if_present(que.get(i).get_vehicle_id(),full_details.get(time)) > -1){
				num_packet_to_send++;
			}
			else{
				System.out.println("PAcket Dropped, Vehicle not present");
				System.out.println("Packet ID: " + que.get(i).get_pack_id() + " \tVehicle ID: " + que.get(i).get_vehicle_id() + "\tTime To Live: " + que.get(i).get_time_to_live() + " \tOrigin Time: " + que.get(i).get_orig_sec() + "\t Tries: " + que.get(i).get_tries() + "\tGroup: " + que.get(i).get_group_num());
				drop_pckts++;
				que.remove(i);
				i--;
			}
			i++;
			if(i >= que.size()){
				break;
			}
		}

		j=0;
		while(que.get(j).get_time_to_live() == 0){
			p = que.get(j);
			num_groups_present = 0;
			//check if groups are present
			for(i=0;i<p.get_group_num().size();i++){
				int t = check_for_repeat(main_list.get(time),main_list.get(p.get_orig_sec()).get(p.get_group_num().get(i)));
				if(t >= 0){
					num_groups_present++;
				}
			}
			if(num_groups_present == 0){//If groups are not present
				System.out.println("Packet dropped,   GRoup not present");
				System.out.println("Packet ID: " + que.get(j).get_pack_id() + " \tVehicle ID: " + que.get(j).get_vehicle_id() + "\tTime To Live: " + que.get(j).get_time_to_live() + " \tOrigin Time: " + que.get(j).get_orig_sec() + "\t Tries: " + que.get(j).get_tries() + "\tGroup: " + que.get(j).get_group_num());
				que.remove(j);
				num_packet_to_send--;
				drop_pckts++;
				j--;
			}
			j++;
			if(j >= que.size()){
				break;
			}
		}




		if(num_packet_to_send == 1){

			///check for number of vehicles in the group
			if(main_list.get(p.get_orig_sec()).get(p.get_group_num().get(0)).size() == 1){
				System.out.println("Packet dropped, Only one vehicle in group");
				System.out.println("Packet ID: " + que.get(0).get_pack_id() + " \tVehicle ID: " + que.get(0).get_vehicle_id() + "\tTime To Live: " + que.get(0).get_time_to_live() + " \tOrigin Time: " + que.get(0).get_orig_sec() + "\t Tries: " + que.get(0).get_tries() + "\tGroup: " + que.get(0).get_group_num());
				drop_pckts++;
			}
			else{
				System.out.println("Packet sent, Only one packet to send");
				calc_t_put(que.get(0));
				System.out.println("Packet ID: " + que.get(0).get_pack_id() + " \tVehicle ID: " + que.get(0).get_vehicle_id() + "\tTime To Live: " + que.get(0).get_time_to_live() + " \tOrigin Time: " + que.get(0).get_orig_sec() + "\t Tries: " + que.get(0).get_tries() + "\tGroup: " + que.get(0).get_group_num());
				total_delay = total_delay+que.get(0).get_delay()+(time - que.get(0).get_orig_sec());
				sent_pckts++;
			}
			que.remove(0);
		}
		else if(num_packet_to_send > 1){
			//System.out.println("Collision function called");
			int t = check_for_collision(main_list,que,time);
			//System.out.println("Collision function ended");

			if(t == -1){
				System.out.println("Collision");
				i=0;
				while(que.get(i).get_time_to_live() < 1){
					if(que.get(i).get_time_to_live() < 0){
						que.remove(i);
					}
					else if(que.get(i).get_time_to_live() == 0){
						sent_pckts++;
						System.out.println("Packet sent");
						calc_t_put(que.get(i));
						System.out.println("Packet ID: " + que.get(i).get_pack_id() + " \tVehicle ID: " + que.get(i).get_vehicle_id() + "\tTime To Live: " + que.get(i).get_time_to_live() + " \tOrigin Time: " + que.get(i).get_orig_sec() + "\t Tries: " + que.get(i).get_tries() + "\tGroup: " + que.get(i).get_group_num());
						total_delay = total_delay+que.get(i).get_delay()+(time - que.get(i).get_orig_sec());
						que.remove(i);
					}
				}
			}
			else{
				System.out.println("No Collision");
				System.out.println("Packet Sent");
				calc_t_put(que.get(i));
				System.out.println("Packet ID: " + que.get(i).get_pack_id() + " \tVehicle ID: " + que.get(i).get_vehicle_id() + "\tTime To Live: " + que.get(i).get_time_to_live() + " \tOrigin Time: " + que.get(i).get_orig_sec() + "\t Tries: " + que.get(i).get_tries() + "\tGroup: " + que.get(i).get_group_num());
				total_delay = total_delay+que.get(i).get_delay()+(time - que.get(i).get_orig_sec());
				i=0;
				while(que.get(i).get_time_to_live() == 0){
					sent_pckts++;
					que.remove(i);
					if( i >= que.size()){
						break;
					}
				}
			}
		}
	}

	static int check_for_collision(ArrayList<ArrayList<ArrayList<vehicle>>> main_list,ArrayList<packet> que, int time){
		packet p;
		packet temp_p;
		int i=0,j,k,m;
		int fl = 0;
		Random rand = new Random();
		ArrayList<Integer> drop_list = new ArrayList<Integer>();
		ArrayList<Integer> switch_list = new ArrayList<Integer>();
		ArrayList<Integer> collision_packets = new ArrayList<Integer>();
		ArrayList<Integer> curr_packet_gnum = new ArrayList<Integer>();
		ArrayList<Integer> temp_packet_gnum = new ArrayList<Integer>();


		while(que.get(i).get_time_to_live() == 0){
			fl = 0;
			curr_packet_gnum.clear();
			p = que.get(i);
			for(j=0;j<p.get_group_num().size();j++){
				int t = check_for_repeat(main_list.get(time),main_list.get(p.get_orig_sec()).get(p.get_group_num().get(j)));
				if(t > -1){
					curr_packet_gnum.add(t);
				}
			}
			j=i+1;
			if(j >= que.size()){
				break;
			}
			while(que.get(j).get_time_to_live() == 0){
				fl=0;
				for(k = 0;k<collision_packets.size();k++){
					if(j == collision_packets.get(k)){
						fl = 1;
						break;
					}
				}
				if(fl == 0){
					temp_p = que.get(j);
					temp_packet_gnum.clear();
					for(k=0;k<temp_p.get_group_num().size();k++){
						int t = check_for_repeat(main_list.get(time),main_list.get(temp_p.get_orig_sec()).get(temp_p.get_group_num().get(k)));
						if(t > -1){
							temp_packet_gnum.add(t);
						}
					}
					for(k=0;k<curr_packet_gnum.size();k++){
						for(m=0;m<temp_packet_gnum.size();m++){
							if(curr_packet_gnum.get(k) == temp_packet_gnum.get(m)){
								if(!collision_packets.contains(i)){
									collision_packets.add(i);
								}
								if(!collision_packets.contains(j)){
									collision_packets.add(j);
								}
							}
						}
					}
				}
				j++;
				if(j >= que.size()){
					break;
				}
			}
			i++;
			if(i >= que.size()){
				break;
			}
		}

		//System.out.println("HEllo");
		for(i=0;i<collision_packets.size();i++){
			int t = que.get(collision_packets.get(i)).set_tries();
			if(t == -1){
				drop_list.add(collision_packets.get(i));
				System.out.println("Packet dropped after 3 tries ");
				drop_pckts++;
				que.get(collision_packets.get(i)).dec_time_to_live();

			}
			else{
				System.out.println("Packet collision......    switch");
				switch_list.add(collision_packets.get(i));
				p = new packet();
				que.get(collision_packets.get(i)).dec_time_to_live();
				temp_p = que.get(collision_packets.get(i));
				//ArrayList<vehicle> list = main_list.get(temp_p.get_orig_sec()).get(temp_p.get_group_num().get(0));
				//t = check_if_present(temp_p.get_vehicle_id(),list);
				//vehicle veh = main_list.get(temp_p.get_orig_sec()).get(temp_p.get_group_num().get(0)).get(t);
				p.set_time_to_live(difs[temp_p.get_event()-1] + ((int)(Math.random() * 10) + 1));
				p.set_vehicle_id(temp_p.get_vehicle_id());
				p.set_event(temp_p.get_event());
				p.set_delay(time - temp_p.get_orig_sec());
				p.set_orig_sec(time);
				p.set_tries(temp_p.get_tries());
				for(j=0;j<main_list.get(time).size();j++){
					for(k=0;k<main_list.get(time).get(j).size();k++){
						if(main_list.get(time).get(j).get(k).get_vehicle_id() == p.get_vehicle_id()){
							p.set_group_num(j);
						}
					}
				}
				p.set_pack_id(temp_p.get_pack_id());
				add_packet(que,p,0);
			}

		}
		if(collision_packets.size()>0){
			return -1;
		}
		else{
			return 1;
		}

	}


	static void add_packet(ArrayList<packet> que, packet p,int c){
		int i=0;
		if(que.size() > 0){
			while(que.get(i).get_time_to_live() <= p.get_time_to_live()){
				i++;
				if(i>=que.size()){
					break;
				}
			}
		}
		if(c>0){
			p.set_pack_id((int)total_pckts);
		}
		System.out.println("New packket Added, PAcket ID: " + p.get_pack_id() + ", Time to live:  " + p.get_time_to_live() + " Vehicle ID: " + p.get_vehicle_id());
		if(que.size() > 0){
			if(i != que.size()){
				que.add(i,p);
			}
			else{
				que.add(p);
			}

		}
		else{
			que.add(p);
		}
		if(c>0){
			total_pckts++;
		}
	}

	static void add_packets_que(ArrayList<packet> que, ArrayList<ArrayList<ArrayList<vehicle>>> main_list, int time, int group_num){
		int i,j,k,fl;
		ArrayList<vehicle> veh_list = main_list.get(time).get(group_num);
		for(i=0;i<veh_list.size();i++){
			fl =0;
			for(j=0;j<que.size();j++){
				if(que.get(j).get_vehicle_id() == veh_list.get(i).get_vehicle_id()){
					fl =1;
					break;
				}
			}
			if(fl == 0){
				packet p = new packet();
				p.set_time_to_live(veh_list.get(i).get_wait_time());
				p.set_vehicle_id(veh_list.get(i).get_vehicle_id());
				p.set_event(veh_list.get(i).get_event());
				p.set_orig_sec(time);
				p.set_group_num(group_num);
				for(j=0;j<main_list.get(time).size();j++){
					if(j != group_num){
						for(k=0;k<main_list.get(time).get(j).size();k++){
							if(main_list.get(time).get(j).get(k).get_vehicle_id() == p.get_vehicle_id()){
								p.set_group_num(j);
							}
						}
					}
				}
				add_packet(que,p,1);
			}
		}
	}


	static void network_implement(ArrayList<ArrayList<ArrayList<vehicle>>> main_list){
		ArrayList<packet> que = new ArrayList<packet>();
		int i,j,k,m;
		int num_packet_to_send;
		for(i=0;i<simulation_time;i++){
			System.out.println("*******************************************");
			System.out.println("At time = " + i);
			num_packet_to_send = 0;
			// checking to send packets
			if(que.size()>0){
				j=0;
				if(que.get(0).get_time_to_live() != 0){
					for(j=0;j<que.size();j++){
						que.get(j).dec_time_to_live();
					}
				}
				else{
					while(que.get(0).get_time_to_live() == 0){
						check_to_send(main_list,que,i);
						if(que.size() <= 0){
							break;
						}
					}
				}
			}

			//Adding packets to que
			for(j=0;j<main_list.get(i).size();j++){
				for(k=0;k<main_list.get(i).get(j).size();k++){
					int fl =0;
					for(m=0;m<que.size();m++){
						if(que.get(m).get_vehicle_id() == main_list.get(i).get(j).get(k).get_vehicle_id()){
							fl =1;
							break;
						}
					}
					if(fl == 0){
						add_packets_que(que,main_list,i,j);
					}
				}
			}
			/*
			System.out.println("\n\n Packets: ");
			for(j=0;j<que.size();j++){
				System.out.println((j+1) + ". Packet ID: " + que.get(j).get_pack_id() + " \tVehicle ID: " + que.get(j).get_vehicle_id() + "\tTime To Live: " + que.get(j).get_time_to_live() + " \tOrigin Time: " + que.get(j).get_orig_sec() + "\t Tries: " + que.get(j).get_tries() + "\tGroup: " + que.get(j).get_group_num());
			}
			System.out.println("*******************************************");
			*/
		}
		total_pckts = total_pckts - que.size();
	}
}
