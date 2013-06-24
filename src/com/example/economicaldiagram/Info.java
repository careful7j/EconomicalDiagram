package com.example.economicaldiagram;

import java.util.HashMap;

import android.graphics.Bitmap;

/**
 * Data storage class. It might better be a database or a separate file but
 * for that amount of data it's optimal decision.
 * 
 * @author careful7j
 * @version 1.00, 13.06.2013
 * @since android 2.3.3
 *
 */
public class Info {
	
			/** Data of one year */
	final static private HashMap< String,Double > EXPORT_2012 =
			new HashMap< String, Double >(){
			private static final long serialVersionUID = 2529169108985900374L;
			{													
		        put("oil&gas", 74.5 );
		        put("metal", 11.0 );
		        put("chemistry", 5.5 );
		        put("machines", 3.5 );
		        put("food", 2.5 );
		        put("wood", 2.0 );
		        put("other", 1.0 );
		    }};
		    /** Data of one year */    
	final static private HashMap< String,Double > IMPORT_2012 =
			new HashMap< String, Double >(){
			private static final long serialVersionUID = -9136541618311592348L;
			{													
		        put("oil&gas", 1.5 );
		        put("metal", 5.5 );
		        put("chemistry", 16.0 );
		        put("machines", 52.0 );
		        put("food", 13.5 );
		        put("wood", 1.5 );
		        put("other", 4.0 );
		        put("textile", 6.0 );
		    }};
		    /** Data of one year */
	final static private HashMap< String,Double > EXPORT_2010 =
			new HashMap< String, Double >(){
			private static final long serialVersionUID = -7608900809766492743L;
			{													
		        put("oil&gas", 71.5 );
		        put("metal", 13.0 );
		        put("chemistry", 6.0 );
		        put("machines", 4.0 );
		        put("food", 2.0 );
		        put("wood", 2.5 );
		        put("other", 1.0 );
		    }};
		    /** Data of one year */	    
	final static private HashMap< String,Double > IMPORT_2010 =
			new HashMap< String, Double >(){
			private static final long serialVersionUID = -1642648112624722095L;
			{													
		        put("oil&gas", 2.5 );
		        put("metal", 7.0 );
		        put("chemistry", 16.5 );
		        put("machines", 45.0 );
		        put("food", 16.0 );
		        put("wood", 2.5 );
		        put("other", 4.5 );
		        put("textile", 6.0 );
		    }};
		    /** Data of one year */
    final static private HashMap< String,Double > EXPORT_2007 =
			new HashMap< String, Double >(){
    		private static final long serialVersionUID = 4009263998796539528L;
			{													
		        put("oil&gas", 64.0 );
		        put("metal", 13.0 );
		        put("chemistry", 6.0 );
		        put("machines", 6.0 );
		        put("food", 3.0 );
		        put("wood", 4.0 );
		        put("other", 3.5 );
		        put("textile", 0.5 );
		    }};
		    /** Data of one year */
	final static private HashMap< String,Double > IMPORT_2007 =
			new HashMap< String, Double >(){
			private static final long serialVersionUID = -2767484331661241361L;
			{													
		        put("oil&gas", 2.5 );
		        put("metal", 8.5 );
		        put("chemistry", 14.0 );
		        put("machines", 47.0 );
		        put("food", 16.0 );
		        put("wood", 3.0 );
		        put("other", 4.0 );
		        put("textile", 5.0 );
		    }};
		    /** Data of one year */
    final static private HashMap< String,Double > EXPORT_2004 =
			new HashMap< String, Double >(){
    		private static final long serialVersionUID = -6170088212151955520L;
			{													
		        put("oil&gas", 56.0 );
		        put("metal", 17.5 );
		        put("chemistry", 7.0 );
		        put("machines", 9.0 );
		        put("food", 2.5 );
		        put("wood", 4.0 );
		        put("other", 3.0 );
		        put("textile", 1.0 );
		    }};
		    /** Data of one year */
    final static private HashMap< String,Double > IMPORT_2004 =
			new HashMap< String, Double >(){
    		private static final long serialVersionUID = -1082177025749045050L;
			{													
		        put("oil&gas", 1.0 );
		        put("metal", 5.5 );
		        put("chemistry", 18.0 );
		        put("machines", 47.0 );
		        put("food", 17.0 );
		        put("wood", 3.5 );
		        put("other", 4.5 );
		        put("textile", 3.5 );
		    }};
		    /** Data of one year */
    final static private HashMap< String,Double > EXPORT_2000 =
			new HashMap< String, Double >(){
    		private static final long serialVersionUID = 4404905131259402521L;
			{													
		        put("oil&gas", 54.0 );
		        put("metal", 17.0 );
		        put("chemistry", 7.0 );
		        put("machines", 8.5 );
		        put("food", 1.5 );
		        put("wood", 4.5 );
		        put("other", 6.5 );
		        put("textile", 1.0 );
		    }};
		    /** Data of one year */
    final static private HashMap< String,Double > IMPORT_2000 =
			new HashMap< String, Double >(){
    		private static final long serialVersionUID = -1296731767726014872L;
			{													
		        put("oil&gas", 1.0 );
		        put("metal", 6.0 );
		        put("chemistry", 19.0 );
		        put("machines", 44.0 );
		        put("food", 18.5 );
		        put("wood", 3.5 );
		        put("other", 4.5 );
		        put("textile", 3.5 );
		    }};
	
		    /** Common data map of all data for all years */
    final static private HashMap< String, HashMap< String, Double> > allData =
    		new HashMap< String, HashMap< String, Double> >() {
    		private static final long serialVersionUID = -3727665024105474925L;
		{
    		put("EXPORT_2012", EXPORT_2012);
    		put("EXPORT_2010", EXPORT_2010);
    		put("EXPORT_2007", EXPORT_2007);
    		put("EXPORT_2004", EXPORT_2004);
    		put("EXPORT_2000", EXPORT_2000);
    		put("IMPORT_2012", IMPORT_2012);
    		put("IMPORT_2010", IMPORT_2010);
    		put("IMPORT_2007", IMPORT_2007);
    		put("IMPORT_2004", IMPORT_2004);
    		put("IMPORT_2000", IMPORT_2000);
    	}
    };
    
    /** 
     * Number of categories all import and export are divided according to
     * official documentation published on ROSSTAT (Russian state statistics) portal.
     */
    final static int CATEGORIES_NUMBER = 8;
		    
    /** Red channel of diagram sectors' colors  */
	final static int[] R = {  99,	 87,	 77,	 60,	43,	34, 28,	121};
	/** Green channel of diagram sectors' colors */
	final static int[] G = {  140,	 113,	 103,	 80,	63,	42,	31, 147};
	/** Blue channel of diagram sectors' colors  */
	final static int[] B = {  170,	 140,	 130,	 100,	90,	61,	36, 172};	

	/** Text headers on right side of the screen */   
	final static private String[] HEADERS = new String[ CATEGORIES_NUMBER ];
	/** Text body on right side of the screen */
	final static private String[] BODIES = new String[ CATEGORIES_NUMBER ];
	/** Diagram headers */
	final static private String[] LABELS = new String[ allData.size() ];
	/** Diagram background */
	static public Bitmap diagramBMP;
	/** Diagram background */
	static public Bitmap animButtonBMP;
	
	/**
	 * Returns all the data about import and export for all years
	 * 
	 * @return Economical data 
	 */
	public static HashMap< String, HashMap< String, Double> > getAllData() {
		return allData;
	}
	
	public static String[] getHeaders() {
		return HEADERS;
	}
	

	public static String[] getBodies() {
		return BODIES;
	}

	public static String[] getLabels() {
		return LABELS;
	}
	
	public static String getHeader( int id ) {
		return HEADERS[ id ];
	}
	
	public static String getBody( int id ) {
		return BODIES [ id ];
	}
	
	public static String getLabel( int id ) {
		return LABELS [ id ];
	}
	
	// DATA initialization:
	static {
		int i = -1;
		HEADERS [ ++i ] = "Прочие продукты";
		HEADERS [ ++i ] = "Древесина и целюлоза";
		HEADERS [ ++i ] = "Химическая промышленность";
		HEADERS [ ++i ] = "Металлы";
		HEADERS [ ++i ] = "Пищевая промышленность";
		HEADERS [ ++i ] = "Нефть, газ, уголь";
		HEADERS [ ++i ] = "Машиностроение";
		HEADERS [ ++i ] = "Текстиль";
		
		i = -1;
		BODIES [ ++i ] = "" +
			"Сюда входит все, что не вошло в иные категории. А именно:" +
			" керамика, изделия из стекла и камня, часы, музыкальные" +
			" инструменты, мебель, игрушки, спортинвентарь, оружие," +
			" боеприпасы, изделия искусства, антиквариат.";
		
		BODIES [ ++i ] = "" +
			"В эту категорию входят древесина, древесный уголь, бумага и картон." +
			" Большую долю экспорта составляет именно необработанная древесина." +
			" Значительную часть импорта составляют изделия из бумаги и картона." +
			" В списках forbes не значится ни одной российской компании данного профиля.";
		
		BODIES [ ++i ] = "" +
			"Кроме химической промышленности, сюда входит производство взрывчатых веществ, " +
			"соединений драгоценных и редкоземельных металлов, фармацевтическая" +
			" промышленность, фото-кино товары, пластмассы, каучук, резина. Компании, " +
			" попавшие в forbes global: Алроса, Норильский Никель, УралКалий, ФосАрго.";
		
		BODIES [ ++i ] = "" +
			"В эту категорию попадают все виды металлов, в том числе драгоценные." +
			" Компании, попавшие в forbes global: РусАЛ, Новолипецкий Металлургический" +
			" Комбинат, Магнитогорский Металлургический Комбинат, Мечел, Северсталь.";
		
		BODIES [ ++i ] = "" +
			"Этот сектор экономики занят производством продуктов питания и сырья для" +
			" их изготовления. В списки forbes нет ни одной крупной экспортирующей" +
			" пищевой компании из России. Но есть крупные компании занятые импортом и" +
			" продажей пищевых продуктов, это: Магнит и \"Икс пять ритэйл\". Последняя является" +
			" нидерландской компанией владеющей около 700 крупными магазинами на территории " +
			" России (Пятерочка, Перекресток, Карусель и др.).";
		
		BODIES [ ++i ] = "" + 
			"Нефть и газ являются основой российской экономики. На этот сектор сегодня" +
			" приходится 3/4 всех доходов страны и его доля стабильно растет из года в год." +
			" Бытует мнение, что в случае падения цен на нефть, страна может оказаться в" +
			" невыгодном положении, однако, конкретно сейчас (2013Q2) уровень жизни в стране" +
			" высок как никогда. Компании, попавшие в forbes global: Газпром (по уровню прибыли" +
			" на одном уровне с гигантом Apple т.е. 3ье место в мире), Роснефть, Лукойл, ТНК-ВР," +
			" СургутНефтеГаз, Татнефть, Новатек, Транснефть.";
		
		BODIES [ ++i ] = "" + 
			"Сюда входят: машины, оборудование, транспортные средства, атомные реакторы," +
			" компьютеры, аудио/фотоаппаратура, космические корабли, самолеты, суда," +
			" инструменты, медицинское оборудование. Россия занимается строительством " +
			" атомных электростанций на территории других государств, производством " +
			" авиалайнеров типа sukhoi superjet 100, доставляет грузы и спутники на орбиту.";
		
		BODIES [ ++i ] = "" +
			"Текстильная промышленность. Сюда входит производство шелка," +
			" шерсти, хлопка, ткани, волокна, трикотаж, одежда, обувь, головные уборы," +
			" зонты, трости, перья, пух, искусственные цветы. На данный момент отрасль " +
			" уже не имеет какой либо значимой доли в экспорте России.";
		
		i = -1;
		LABELS[ ++i ] = "Структура импорта Российской Федерации 2012";
		LABELS[ ++i ] = "Структура импорта Российской Федерации 2010";
		LABELS[ ++i ] = "Структура импорта Российской Федерации 2007";
		LABELS[ ++i ] = "Структура импорта Российской Федерации 2004";
		LABELS[ ++i ] = "Структура импорта Российской Федерации 2000";
		LABELS[ ++i ] = "Структура экспорта Российской Федерации 2012";
		LABELS[ ++i ] = "Структура экспорта Российской Федерации 2010";
		LABELS[ ++i ] = "Структура экспорта Российской Федерации 2007";
		LABELS[ ++i ] = "Структура экспорта Российской Федерации 2004";
		LABELS[ ++i ] = "Структура экспорта Российской Федерации 2000";
		
	}	

}
