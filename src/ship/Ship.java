package ship;

import java.io.File;

import fileHelper.FileHelper;
import interfaces.ShipTemplate;

/*
 *    Plik: Ship.java
 *          
 *   Autor: Miron Oskroba
 *    Data: listopad 2019 r.
 */
public class Ship implements ShipTemplate{
	/*
	 * 		Ship.java stores data of ship objects
	 */
	
	//public static inner enum OR just public enum in a separate file
	public static enum ShipType {
		UNKNOWN("--"),
		CARGO("Cargo"),
		PASSENGER("Passenger"),
		TANKER("Tanker"),
		FISHING("Fishing"),
		RACING("Racing");
		;

		String type;

		private ShipType(String shipType) {
			this.type = shipType;
		}

		@Override
		public String toString() {
			return type;
		}
	}
	
	public static final int DATA_AMOUNT = 5; // 5 fields name, shiptype etc..
	
	private String name;
	
	private int crew;//people needed on board
	private int maxCrew = 2000; // maximum amount of 2000 people in crew
	
	private int beds;
	private int maxBeds = 5000; // maximum amount of beds 5000 on ship

	private String constructionYear;
	private ShipType type;
	
	private FileHelper fileHelper;
	
	public Ship(String name, String shipType, String constructionYear, String crew, String beds, FileHelper fileHelper) throws ShipException{
		setName(name);
		setShipType(shipType.toLowerCase());
		setConstructionYear(constructionYear);
		setCrew(crew);
		setBeds(beds);
		
		this.fileHelper = fileHelper;
	}
	
	@Override
	public void setName(String name) throws ShipException{ 
		if(name == null || name.equals("")){
			throw new ShipException("Pole imi� musi by� wype�nione!");
		}
		this.name = name;
	}
	
	@Override
	public void setShipType(String shipType) throws ShipException{
		for(ShipType t : ShipType.values()) {
			if((shipType.toLowerCase()).equals(t.toString().toLowerCase())) {
				this.type = t;
				return;
			}
		}
		if(shipType == null)
			throw new ShipException("Podany typ statku nie istnieje.");
	}
	
	@Override
	public void setConstructionYear(String constructionYear) throws ShipException{
		int year = Integer.parseInt(constructionYear);
		if(year<1800||year>2019)
			throw new ShipException("Rok musi by� w przedziale 1800-2019.");
		this.constructionYear = constructionYear;
	}
	
	@Override
	public void setCrew(String crew) throws ShipException{
		int crewNum = Integer.parseInt(crew);
		if(crewNum < 0 || crewNum > maxCrew)
			throw new ShipException("Liczebno�� za�ogi musi by� w przedziale 0-"+maxCrew);
		this.crew = crewNum;
	}
	
	@Override
	public void setBeds(String beds) throws ShipException{
		int bedsNum = Integer.parseInt(beds);
		if(bedsNum < 0 || bedsNum > maxBeds)
			throw new ShipException("Liczba ��ek musi by� w przedziale 0-"+maxBeds);
		if(bedsNum<crew)
			throw new ShipException("Za ma�o ��ek na tak� ilo�� za�ogi. Nie wszyscy b�d� mieli gdzie spa�!");
		this.beds = bedsNum;
	}
	
	@Override
	public void saveToFile() {
		fileHelper.createTextFile(getFile(), getFileData());
	}
	
	@Override
	public final File getFile() {
		return new File(fileHelper.SHIPS_DIRECTORY + "/" + name + " " + type.toString() + " " + constructionYear + ".txt");
	}
	
	@Override
	public FileHelper getFileHelper() { return fileHelper;}

	private final String[] getFileData() {
		return new String [] {
				"imie:" + name + ";",
				"typ_statku:" + type.toString()  + ";",
				"rok_konstrukcji:" + constructionYear + ";", 
				"liczebnosc_zalogi:" + crew + ";",
				"liczba_lozek:" + beds + ";"
							};
	}
	
	public String []getData(){
		String[] data = {name,type.toString(),constructionYear, Integer.toString(crew),Integer.toString(beds)};
		return data;
	}
	
	@Override
	public String getName() {return name;}
	
	@Override
	public int getBeds() {return beds;}
	
	@Override
	public ShipType getShipType() { return type;}

	@Override
	public String getConstructionYear() {return constructionYear;}

	@Override
	public int getCrew() {return crew;}
	
	@Override
	public boolean equals(Ship ship) {
		if(name.equals(ship.getName()) && type == ship.getShipType() && constructionYear.equals(ship.getConstructionYear()) && crew == ship.getCrew() && beds == ship.getBeds())
			return true;
		return false;
	}

	@Override
	public String toString() {
		return  "1.Imi�:" + name + 
				"\n2.Typ statku:"  + type.toString() + 
				"\n3.Rok konstrukcji:" + constructionYear +
				"\n4.Liczebno�� za�ogi: " + crew +
				"\n5.Liczba ��ek:" + beds+"\n";
	}
	
	//public static inner class OR just public in a separate file
		public static class ShipException extends Exception{
			/*
			 * 		ShipException.java is used to throw ShipException when ship is being created and the input data does not match Ship standards
			 */
			//powininen byc zapisany domyslnie skrot klasy 
			//mechanizm serializacji, obiekt klasy mozna zamienic na ciag bajtow, zapisac go 
			//lub wyslac a pozniej mozna ten ciag odczytac i odtworzyc obiekt
			//problem serializacji - brak zgodno�ci, ciag bajtow nie pasuje do nowej wersji klasy
			//zapamietany skrot aktualnej wersji klasy pozwala na to
			private static final long serialVersionUID = 1L;
			
			ShipException(String message){
				super(message);
			}
		}
}
