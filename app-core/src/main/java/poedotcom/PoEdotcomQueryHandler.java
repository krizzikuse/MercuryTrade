/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package poedotcom;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import currencydata.CurrencyAmount;
import currencydata.CurrencyData;
import static helpful_techniques.FileHelper.getPath;
import helpful_techniques.JSONBeautifier;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import javax.net.ssl.SSLContext;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
import poedotcom.queryrequest.QueryRequest;
import poedotcom.queryrequest.query.filters.FiltersField;
import poedotcom.queryrequest.query.filters.armour.ArmourField;
import poedotcom.queryrequest.query.filters.misc.MiscField;
import poedotcom.queryrequest.query.filters.req.ReqField;
import poedotcom.queryrequest.query.filters.socket.SocketField;
import poedotcom.queryrequest.query.filters.trade.TradeField;
import poedotcom.queryrequest.query.filters.type.TypeField;
import poedotcom.queryrequest.query.filters.type.TypeFiltersField.Category;
import poedotcom.queryrequest.query.filters.type.TypeFiltersField.Rarity;
import poedotcom.queryrequest.query.filters.weapon.WeaponField;
import poedotcom.queryrequest.query.stats.StatsFiltersField;

//"C:\Mercury Trade Enahncements\MercuryTrade-master\app-core\src\main\java\poedotcom\queryrequest\query\StatusField.java"
import poedotcom.queryrequest.query.StatusField.StatusValues;
import static java.lang.System.exit;
import java.util.HashMap;
import java.util.Map;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import poedotcom.basictypes.Enums.QueryType;
import poedotcom.basictypes.PoEdotcomIDText;
import poedotcom.queryrequest.BulkQueryRequest;
import poedotcom.queryrequest.query.filters.trade.PriceRangeField;
import poedotcom.queryrequest.query.filters.trade.StashInfoField;
/**
 *
 * @author Chris (IGN/Accountname: weedkrizz)
 */
public class PoEdotcomQueryHandler {
    private final org.apache.logging.log4j.Logger logger = LogManager.getLogger(PoEdotcomQueryHandler.class.getSimpleName());

    CloseableHttpClient httpclient = null;
    SSLContext sslcontext;
    //String postData = "{ \"query\": { \"status\": { \"option\": \"online\" }, \"name\": \"The Pariah\", \"type\": \"Unset Ring\", \"stats\": [{ \"type\": \"and\", \"filters\": [] }] }, \"sort\": { \"price\": \"asc\" } }";
    String postData = "";//{ \"query\": { \"status\": { \"option\": \"online\" }, \"name\": \"The Pariah\", \"type\": \"Unset Ring\", \"stats\": [{ \"type\": \"and\", \"filters\": [] }] }, \"sort\": { \"price\": \"asc\" } }";
    HttpHost target;
    Gson gson = new Gson();
    SSLConnectionSocketFactory sslConnectionSocketFactory;
    public PoEdotcomQueryHandler() throws IOException {
//        
//        System.setProperty("file.encoding", "ASCII"); //probably useless
//        Gson gson = new Gson();
//        QueryRequest myQuery = new QueryRequest();
//        
//        ArrayList<StatsFiltersField> statfilters = new ArrayList<StatsFiltersField>();
//        
//        StatsFiltersField statfilter1 = new StatsFiltersField();
//        StatsFiltersField statfilter2 = new StatsFiltersField();
//        
//        statfilter1.set("pseudo.pseudo_total_attack_speed",1, 9999,false);
//        statfilters.add(statfilter1);
//        
//        statfilter2.set("pseudo.pseudo_count_elemental_resistances",1, 9999,false);
//        statfilters.add(statfilter2);   //TODO ? dont do this if you want anything as a response!
//        
//        FiltersField filters = new FiltersField();
//        
//        WeaponField weaponfilters = new WeaponField();
//        weaponfilters.addWeaponFilter(WeaponField.Type.damage,1,9999);
//        weaponfilters.addWeaponFilter(WeaponField.Type.crit,1,9999);
//        weaponfilters.addWeaponFilter(WeaponField.Type.aps,1,9999);
//        weaponfilters.addWeaponFilter(WeaponField.Type.dps,1,9999);
//        weaponfilters.addWeaponFilter(WeaponField.Type.edps,1,9999);
//        weaponfilters.addWeaponFilter(WeaponField.Type.pdps,1,9999);
//        filters.setWeapon_filters(weaponfilters);
//        
//        
//        ArmourField armourfilters = new ArmourField();
//        armourfilters.addArmourFilter(ArmourField.Type.ar,1,9999);
//        armourfilters.addArmourFilter(ArmourField.Type.es,1,9999);
//        armourfilters.addArmourFilter(ArmourField.Type.ev,1,9999);
//        armourfilters.addArmourFilter(ArmourField.Type.block,1,9999);
//        filters.setArmour_filters(armourfilters);
//        
//        
//        SocketField socketfilters = new SocketField();
//        socketfilters.addSocketFilter(SocketField.Type.sockets, 
//                (byte)0, (byte)0, (byte)0, (byte)0, (byte)0, (byte)0);
//        socketfilters.addSocketFilter(SocketField.Type.links, 
//                (byte)0, (byte)0, (byte)0, (byte)0, (byte)0, (byte)0);
//        filters.setSocket_filters(socketfilters);
//        
//        
//        ReqField reqfilters = new ReqField();
//        reqfilters.addReqFilter(ReqField.Type.lvl, (byte)1, (byte)99);
//        reqfilters.addReqFilter(ReqField.Type.dex, (byte)1, (byte)9999);
//        reqfilters.addReqFilter(ReqField.Type.str, (byte)1, (byte)9999);
//        reqfilters.addReqFilter(ReqField.Type.intField, (byte)1, (byte)9999);
//        filters.setReq_filters(reqfilters);
//        
//        
//        MiscField miscfilters = new MiscField();
//        miscfilters.addMiscFilter(MiscField.Type.quality,(byte)15,(byte)20);
//        miscfilters.addMiscFilter(MiscField.Type.map_tier,(byte)12,(byte)14);
//        miscfilters.addMiscFilter(MiscField.Type.map_iiq,(short)80,(short)120);
//        miscfilters.addMiscFilter(MiscField.Type.gem_level,(byte)20,(byte)21);
//        miscfilters.addMiscFilter(MiscField.Type.ilvl,(byte)83,(byte)86);
//        miscfilters.addMiscFilter(MiscField.Type.map_packsize,(short)38,(short)50);
//        miscfilters.addMiscFilter(MiscField.Type.map_iir,(short)125,(short)200);
//        miscfilters.addMiscFilter(MiscField.Type.talisman_tier,(byte)2,(byte)4);
//        
//        miscfilters.addMiscFilter(MiscField.Type.alternate_art,false);
//        miscfilters.addMiscFilter(MiscField.Type.identified,true);
//        miscfilters.addMiscFilter(MiscField.Type.corrupted,false);
//        miscfilters.addMiscFilter(MiscField.Type.crafted,true);
//        miscfilters.addMiscFilter(MiscField.Type.enchanted,true);
//        filters.setMisc_filters(miscfilters);
//        
//        
//        TradeField tradefilters = new TradeField();
////        tradefilters.addTradeFilter(TradeField.Type.account, "weedkrizz");
//        tradefilters.addTradeFilter(TradeField.Type.sale_type, "priced");
//        PriceRangeField priceRange = new PriceRangeField();
//        priceRange.setOption("exa");
//        priceRange.setMin(500);
//        priceRange.setMax(5000);
//        //priceRange.setType(postData);
//        
//        tradefilters.addTradeFilter(TradeField.Type.price, priceRange);
//        filters.setTrade_filters(tradefilters);
//        
//        TypeField typefilters = new TypeField();
//        typefilters.addTypeFilter(TypeField.Type.category, Category.weapon_twoaxe);
//        typefilters.addTypeFilter(TypeField.Type.rarity, Rarity.rare);
//        filters.setType_filters(typefilters);
//
//        //myQuery.makeQuery("online","Kaom's Primacy","Karui Chopper","and",statfilters,filters,QueryRequest.Order.asc);
//        // most tested version is this:
//        myQuery.makeQuery(StatusValues.online,"Kaom's Primacy","Karui Chopper","and",statfilters,filters,QueryRequest.Order.asc);
        
        //not needed and also impossible on official site (that uses this same official TRADING-API)
        // => possible on trading sites - but those use the humongous PUBLIC STASH TAB API
        //-> meant are the following 2 lines, and all lines that have to do with the processing resulting from those 2 lines!
        // StashInfoField stash = new StashInfoField();
        // stash.setName("Sale");
        
        //myQuery.makeQuery(StatusValues.online,"and",filters,QueryRequest.Order.asc);

        //DEBUG-PART for DEVELOPMENT start
//        String test = gson.toJson(myQuery);
//        System.out.println("##### now printing request #########################\n"
//                + JSONBeautifier.format(test) 
//                + "\n##### end of request #########################");
        //exit(0);
        
//        postData = test;
        //DEBUG-PART for DEVELOPMENT end

        //source: https://stackoverflow.com/questions/14561293/sending-post-data-to-https-without-ssl-cert-verification-with-apache-httpclient
        //and also: https://www.reddit.com/r/pathofexiledev/comments/7aiil7/how_to_make_your_own_queries_against_the_official/
        /////////////////
        // Create SSL Client
        /////////////////
        target = new HttpHost("www.pathofexile.com", 443, "https");
        sslcontext = SSLContexts.createSystemDefault();
        sslConnectionSocketFactory = new SSLConnectionSocketFactory(
                sslcontext, new String[] { "TLSv1", "SSLv3" }, null,
                SSLConnectionSocketFactory.getDefaultHostnameVerifier());

        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.INSTANCE)
                .register("https", sslConnectionSocketFactory)
                .build();
        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager(socketFactoryRegistry);

        httpclient = HttpClients.custom()
        .setSSLSocketFactory(sslConnectionSocketFactory)
        .setConnectionManager(cm)
        .build();

//        /////////////////
//        // Send POST
//        /////////////////
//        HttpPost httppost = new HttpPost("/api/trade/search/Standard");
//        StringEntity requestEntity = new StringEntity(
//            postData,
//            ContentType.APPLICATION_JSON);        
//        httppost.setEntity(requestEntity);
//        CloseableHttpResponse response = httpclient.execute(target, httppost);
//
//        /////////////////
//        // Get RESPONSE
//        /////////////////
//        try {
//            HttpEntity entity = response.getEntity();
//            String result = EntityUtils.toString(entity);
//            System.out.println(result);
            
            //getPoEdotcomStats();  //TODO: make a Button for this Method (update Mod-Data!)
            
//            readResponse(result);
//            EntityUtils.consume(entity);
//        } finally {
            //PoEdotcomQueryHandler_getOffers(QueryType.item,"aphrodite_1289","Blackheart");    //works like that                       
//            getOffers(QueryType.currency,"aphrodite_1289","Orb of Alchemy");                        
//            response.close();
//        }               
    }
    public PoEdotcomOffers getGemOffers(String league, QueryType queryType, 
            String accountName, String gemname, String gemlevel, String gemquality) throws IOException {
        return(getGemOffers(StatusValues.online,league,queryType,accountName,
                gemname,gemlevel,gemquality));
    } 
    public PoEdotcomOffers getGemOffers(StatusValues status,String league, QueryType queryType, 
            String accountName, String gemname, String gemlevel, String gemquality) throws IOException {
        PoEdotcomOffers offers = new PoEdotcomOffers();
        
        System.setProperty("file.encoding", "ASCII"); //probably useless
        Gson gson = new Gson();
        QueryRequest myQuery = new QueryRequest();
        
//        ArrayList<StatsFiltersField> statfilters = new ArrayList<StatsFiltersField>();
    
        FiltersField filters = new FiltersField();
        TradeField tradefilters = new TradeField();
        
        //most likely not needed, for this feature at least => kept for now! TODO?
        //tradefilters.addTradeFilter(TradeField.Type.sale_type, "priced");
        
        //the following 5 lines are not needed for this method(price verification)
//        PriceRangeField priceRange = new PriceRangeField();
//        priceRange.setOption("exa");
//        priceRange.setMin(50);
//        priceRange.setMax(300);
//        tradefilters.addTradeFilter(TradeField.Type.price, priceRange);
        tradefilters.addTradeFilter(TradeField.Type.account, accountName.trim());
        filters.setTrade_filters(tradefilters);   
        MiscField miscfilters = new MiscField();
        //miscfilters.addMiscFilter(MiscField.Type.quality,Byte.parseByte(gemquality),Byte.parseByte(gemquality));     //!!!TODO!!! put it back in as soon as GGG fixes the filter!
        miscfilters.addMiscFilter(MiscField.Type.gem_level,Byte.parseByte(gemlevel),Byte.parseByte(gemlevel));
        filters.setMisc_filters(miscfilters);
        
        myQuery.makeCurrencyQuery(status,gemname.trim(), filters, QueryRequest.Order.asc); 
        
        //DEBUG-PART for DEVELOPMENT start
        String jsonString = gson.toJson(myQuery);
//        System.out.println("##### now printing request #########################\n"
//                + JSONBeautifier.format(jsonString) 
//                + "\n##### end of request #########################");
        logger.log(Level.DEBUG,"##### now printing request #########################\n"
                + JSONBeautifier.format(jsonString) 
                + "\n##### end of request #########################");
        
        //exit(0);
        //DEBUG-PART for DEVELOPMENT end        
        /////////////////
        // Send POST
        /////////////////
        //HttpPost httppost = new HttpPost("/api/trade/search/Standard");
        HttpPost httppost = new HttpPost("/api/trade/search/"+league.trim());
        StringEntity requestEntity = new StringEntity(
            jsonString,
            ContentType.APPLICATION_JSON);        
        httppost.setEntity(requestEntity);
        CloseableHttpResponse response = httpclient.execute(target, httppost);

        /////////////////
        // Get RESPONSE
        /////////////////
        try {
            HttpEntity entity = response.getEntity();
            String result = EntityUtils.toString(entity);
//            System.out.println(result);
            logger.log(Level.DEBUG,result);
            
            //getPoEdotcomStats();  //TODO: make a Button for this Method (update Mod-Data!)
            
            readResponse(result,offers,queryType);
            EntityUtils.consume(entity);
            
        } finally {
            response.close();
            return offers;
        }                    
    }
    
    MiscField mapMiscfilters;
    public void addMapDetails(String map, String tier, String amount) {
        mapMiscfilters = new MiscField();
        if ((tier!=null) && (!tier.equals(""))) {
            mapMiscfilters.addMiscFilter(MiscField.Type.map_tier,(byte)Integer.parseInt(tier),(byte)Integer.parseInt(tier)); 
        }
    }
    public PoEdotcomOffers getOffers(String league, QueryType queryType, String accountName, String itemName) throws IOException {
        return(getOffers(StatusValues.online,league,queryType,accountName,itemName));
    }             
    //for PriceCheck (price-verification as seen in PoE-Trades-Companion
    public PoEdotcomOffers getOffers(StatusValues status,String league, QueryType queryType, String accountName, String itemName) throws IOException { 
    //        String stashName, byte stashX, byte stashY) throws IOException {  
        //needed parameters:                                                    
        //|<-----TradeFilters----->|<-query->|<------(probably)TradeFilters------>|   
        // accountname/lastCharName, itemname, location in stash(x+y-coords)+stash
        //price (but this only once we have the results - probably not here, 
        //but in a different class (e.g. only return the real price from this function
        //and the comparing is done by the caller!
        // => official API cannot filter for stash (and classes should only know what they need to know!)
        //  so we'll compare the matching (in accountName + itemName matching) in the caller-class 
        if (queryType == QueryType.exchange)
            ;//!!TODO!! implement bulk-query (see JSON-structure at the end of this class)

        PoEdotcomOffers offers = new PoEdotcomOffers();
        
        System.setProperty("file.encoding", "ASCII"); //probably useless
        Gson gson = new Gson();
        QueryRequest myQuery = new QueryRequest();
        
//        ArrayList<StatsFiltersField> statfilters = new ArrayList<StatsFiltersField>();
    
        FiltersField filters = new FiltersField();
        TradeField tradefilters = new TradeField();
        
        //most likely not needed, for this feature at least => kept for now! TODO?
        //if (queryType != QueryType.currency)
        //    tradefilters.addTradeFilter(TradeField.Type.sale_type, "priced");
        
        //the following 5 lines are not needed for this method(price verification)
//        PriceRangeField priceRange = new PriceRangeField();
//        priceRange.setOption("exa");
//        priceRange.setMin(50);
//        priceRange.setMax(300);
//        tradefilters.addTradeFilter(TradeField.Type.price, priceRange);
        tradefilters.addTradeFilter(TradeField.Type.account, accountName.trim());
        filters.setTrade_filters(tradefilters);   
        
        if (queryType == QueryType.mapitem) {
            filters.setMisc_filters(mapMiscfilters);  
        }
        
        if (queryType == QueryType.item)
            myQuery.makeQuery(status,itemName.trim(), filters, QueryRequest.Order.asc); 
        else if ((queryType == QueryType.currency) || (queryType == QueryType.mapitem))
            myQuery.makeCurrencyQuery(status,itemName.trim(), filters, QueryRequest.Order.asc); 
            //myQuery.makeCurrencyQuery(status,itemName.trim(), QueryRequest.Order.asc);
        
        //DEBUG-PART for DEVELOPMENT start
        String jsonString = gson.toJson(myQuery);
        logger.log(Level.DEBUG,"##### now printing request #########################\n"
                + JSONBeautifier.format(jsonString) 
                + "\n##### end of request #########################");
//        System.out.println("##### now printing request #########################\n"
//                + JSONBeautifier.format(jsonString) 
//                + "\n##### end of request #########################");
        
        //exit(0);
        //DEBUG-PART for DEVELOPMENT end
        
        /////////////////
        // Send POST
        /////////////////
        //HttpPost httppost = new HttpPost("/api/trade/search/Standard");
        HttpPost httppost = new HttpPost("/api/trade/search/"+league.trim());
        StringEntity requestEntity = new StringEntity(
            jsonString,
            ContentType.APPLICATION_JSON);        
        httppost.setEntity(requestEntity);
        CloseableHttpResponse response = httpclient.execute(target, httppost);

        /////////////////
        // Get RESPONSE
        /////////////////
        try {
            HttpEntity entity = response.getEntity();
            String result = EntityUtils.toString(entity);
//            System.out.println(result);
            logger.log(Level.DEBUG,result);
            
            //getPoEdotcomStats();  //TODO: make a Button for this Method (update Mod-Data!)
            
            readResponse(result,offers,queryType);
            EntityUtils.consume(entity);
            
        } finally {
            response.close();
            return offers;
        }            
    }
    public void readResponse(String source, PoEdotcomOffers poedotComOffers, QueryType queryType) throws IOException {
    //see https://stackoverflow.com/questions/5796948/how-to-parse-dynamic-json-fields-with-gson            
        String queryTarget = queryType == QueryType.exchange ? "exchange" : "query";
        Gson gson = new Gson();
        Type mapType = new TypeToken<PoEdotcomQueryResults>() {}.getType();
        JsonParser jsonParser = new JsonParser();
        JsonReader reader = new JsonReader(new StringReader(source));
        PoEdotcomQueryResults poeDotComQueryResults = gson.fromJson(jsonParser.parse(reader), mapType);
        logger.log(Level.DEBUG,"#####" + " now querying all "
                + poeDotComQueryResults.getTotal() + " returned listings ##########");
//        System.out.println("#####" + " now querying all "
//                + poeDotComQueryResults.getTotal() + " returned listings ##########");
        //https://www.pathofexile.com/api/trade/fetch/RESULT_LINES_HERE?query=ID_HERE
        //  where RESULT_LINES_HERE is all the elements in the 
        //  returned result array joined by comma (,) and the query parameter 
        //is the string returned as ID. So a complete fetch would be
        //https://www.pathofexile.com/api/trade/fetch/e90f6f29233424e9d85b1d488aab29e33edededf06f08a4aaf75d9bb67c251db,dbb04462a152cdf58f5960d9b23a70d2ce30d78ae0b8c71dc13ed392d3fff4c1?query=vnakwfm
        //PoEdotcomOffers poedotComOffers = new PoEdotcomOffers();
        while (poeDotComQueryResults.getResult().size()>0) {
            String allresults = "";
            for (int i=0;(i<10 && poeDotComQueryResults.getResult().size()>0);i++)  //TODO evaluate how many listings can be queried at once!
                if (i>0)
                    allresults += "," + poeDotComQueryResults.getResult().remove(0);
                else
                    allresults += poeDotComQueryResults.getResult().remove(0);
            
            String offerSearchURLstring = "/api/trade/fetch/" + allresults 
                    + "?" + queryTarget + "=" + poeDotComQueryResults.getId();
            
            try {
                poedotComOffers.addResult(queryPoEdotcom(offerSearchURLstring).result);
            } catch (Exception ex) {
                logger.log(Level.ERROR,"Exception occured in PoEdotcomQueryHandler.readResponse:\n" 
                        + ex + "\n returning all gathered data, at least ...");
//                System.out.println("Exception occured in PoEdotcomQueryHandler.readResponse:\n" 
//                        + ex + "\n returning all gathered data, at least ...");
//                return(poedotComOffers);
            }
        }

        int i=0;
        logger.log(Level.DEBUG,"##### SUCCESS => all " + poeDotComQueryResults.getTotal() + " listings queried!");
//        System.out.println("##### SUCCESS => all " + poeDotComQueryResults.getTotal() + " listings queried!");
//        return(poedotComOffers);
        
        //return(null);
    }    
    public void readResponse(String source) throws IOException {
    //see https://stackoverflow.com/questions/5796948/how-to-parse-dynamic-json-fields-with-gson            
        
        Gson gson = new Gson();
        Type mapType = new TypeToken<PoEdotcomQueryResults>() {}.getType();
        JsonParser jsonParser = new JsonParser();
        JsonReader reader = new JsonReader(new StringReader(source));
        PoEdotcomQueryResults poeDotComQueryResults = gson.fromJson(jsonParser.parse(reader), mapType);
        logger.log(Level.DEBUG,"#####" + " now querying all "
                + poeDotComQueryResults.getTotal() + "returned listings ##########");
//        System.out.println("#####" + " now querying all "
//                + poeDotComQueryResults.getTotal() + "returned listings ##########");
        //https://www.pathofexile.com/api/trade/fetch/RESULT_LINES_HERE?query=ID_HERE
        //  where RESULT_LINES_HERE is all the elements in the 
        //  returned result array joined by comma (,) and the query parameter 
        //is the string returned as ID. So a complete fetch would be
        //https://www.pathofexile.com/api/trade/fetch/e90f6f29233424e9d85b1d488aab29e33edededf06f08a4aaf75d9bb67c251db,dbb04462a152cdf58f5960d9b23a70d2ce30d78ae0b8c71dc13ed392d3fff4c1?query=vnakwfm
        PoEdotcomOffers poedotComOffers = new PoEdotcomOffers();
        while (poeDotComQueryResults.getResult().size()>0) {
            String allresults = "";
            for (int i=0;(i<10 && poeDotComQueryResults.getResult().size()>0);i++)  //TODO evaluate how many listings can be queried at once!
                if (i>0)
                    allresults += "," + poeDotComQueryResults.getResult().remove(0);
                else
                    allresults += poeDotComQueryResults.getResult().remove(0);
            
            String offerSearchURLstring = "/api/trade/fetch/" + allresults + "?query=" + poeDotComQueryResults.getId();
            
            try {
                poedotComOffers.addResult(queryPoEdotcom(offerSearchURLstring).result);
            } catch (Exception ex) {
                logger.log(Level.ERROR,"Exception occured in PoEdotcomQueryHandler.readResponse:\n" 
                        + ex + "\n returning all gathered data, at least ...");
//                System.out.println("Exception occured in PoEdotcomQueryHandler.readResponse:\n" 
//                        + ex + "\n returning all gathered data, at least ...");
//                return(poedotComOffers);
            }
        }

        int i=0;
        logger.log(Level.DEBUG,"##### SUCCESS => all " + poeDotComQueryResults.getTotal() + " listings queried!");
//        return(poedotComOffers);
        
        //return(null);
    }    

    public PoEdotcomOffers queryPoEdotcom(String httpPostString) throws IOException {
        /////////////////
        // Send GET
        /////////////////
        HttpGet httpPost = new HttpGet(httpPostString);    
        CloseableHttpResponse response = null;
        response = httpclient.execute(target, httpPost);
            
//        System.out.println(response.toString());
        logger.log(Level.DEBUG,response.toString());

        /////////////////
        // Get RESPONSE
        /////////////////
        HttpEntity entity = response.getEntity();
        String result = EntityUtils.toString(entity);
        logger.log(Level.DEBUG,JSONBeautifier.format(result)); 
        
        EntityUtils.consume(entity);

        Gson gson = new Gson();
        Type mapType = new TypeToken<PoEdotcomOffers>() {}.getType();
        JsonParser jsonParser = new JsonParser();
        JsonReader reader = new JsonReader(new StringReader(result));
        //JsonObject jsonObj = gson.fromJson (jsonParser.parse(reader), JsonElement.class).getAsJsonObject();
        PoEdotcomOffers poedotComOffers = gson.fromJson(jsonParser.parse(reader), mapType);  

        response.close();
        //System.out.println(poedotComOffers.result.get(0).getId());

        return(poedotComOffers);
    }
    
    // Queries all Mods from GGG 
    //    https://www.pathofexile.com/api/trade/data/stats
    public void getPoEdotcomStats() throws IOException {
        //https://www.pathofexile.com/api/trade/data/stats
        /////////////////
        // Send GET
        /////////////////
        HttpGet httpPost = new HttpGet("/api/trade/data/stats");    
        CloseableHttpResponse response = null;
        response = httpclient.execute(target, httpPost);
            
        /////////////////
        // Get RESPONSE
        /////////////////
        HttpEntity entity = response.getEntity();
        String result = EntityUtils.toString(entity);
//        System.out.println(result); 
        logger.log(Level.DEBUG,result); 
        
        EntityUtils.consume(entity);

        Gson gson = new Gson();
        Type mapType = new TypeToken<PoEdotcomOffers>() {}.getType();
        JsonParser jsonParser = new JsonParser();
        JsonReader reader = new JsonReader(new StringReader(result));
        //JsonObject jsonObj = gson.fromJson (jsonParser.parse(reader), JsonElement.class).getAsJsonObject();
        JsonObject modStats = gson.fromJson(jsonParser.parse(reader), JsonElement.class).getAsJsonObject();  
        
        response.close();
//        System.out.println("######################### now showing all mods queried from www.pathofexile.com/api/trade/data/stats #########################");
        logger.log(Level.DEBUG,"######################### now showing all mods queried from www.pathofexile.com/api/trade/data/stats #########################");
        //System.out.println(JSONBeautifier.fomat(modStats.toString()));
        String jsonString = JSONBeautifier.format(modStats.toString());
        logger.log(Level.DEBUG,jsonString);
        logger.log(Level.DEBUG,"######################### done showing all mods queried from www.pathofexile.com/api/trade/data/stats #########################");
//        System.out.println(jsonString);
//        System.out.println("######################### done showing all mods queried from www.pathofexile.com/api/trade/data/stats #########################");
        
        
        PrintWriter out = new PrintWriter(getPath("Enhancements_config/json/pathofexile.com.API.Trade.Data.Stats.json"));
        out.println(jsonString);
        out.flush();
        out.close();
        //System.out.println(poedotComOffers.result.get(0).getId());
            
    }
    
    public JsonObject getItemData() throws IOException {
        //poe.ninja/api/Data/GetCurrencyOverview?league=Betrayal
        JsonObject result = getPoEdotcomData("/api/trade/data/items",true,
                        "Enhancements_config/json/"
                        + "pathofexile.com.API.Trade.Data.Items.json"); 
        
        return result;
    }
    
    public PoEdotcomLeagueData getLeagueData() throws IOException {
        //www.pathofexile.com/api/trade/data/leagues
        JsonObject result = getPoEdotcomData("/api/trade/data/leagues",true,
                        "Enhancements_config/json/"
                        + "pathofexile.com.API.Trade.Data.Items.json"); 
        Type mapType = new TypeToken<PoEdotcomLeagueData>() {}.getType();
        JsonParser jsonParser = new JsonParser();
        PoEdotcomLeagueData leagues = gson.fromJson(result, mapType);        
                
        
        return leagues;
    }
    
    public Map<String,String> getCurrencyMapData() throws IOException {
        //https://www.pathofexile.com/api/trade/data/static
        JsonObject result = getPoEdotcomData("/api/trade/data/static",true,
                        "Enhancements_config/json/"
                        + "pathofexile.com.API.Trade.Data.Static.json"); 
        
        gson = new Gson();
//        Type mapType = new TypeToken<PoEdotcomCurrencyData>() {}.getType();
//        JsonParser jsonParser = new JsonParser();
//        //JsonReader reader = new JsonReader(new StringReader(jsonStr));
//        PoEdotcomLeagueData currenciesmaps = gson.fromJson(result, mapType);

//        boolean debug = true;
//        if (debug) {
            //Type mapType = new TypeToken<Map<String,Map<String,ArrayList<Object>>>>() {}.getType();
            Type mapType = new TypeToken<Map<String,Map<String,ArrayList<PoEdotcomIDText>>>>() {}.getType();
            JsonParser jsonParser = new JsonParser();
            Map<String,Map<String,ArrayList<PoEdotcomIDText>>> currenciesmaps = gson.fromJson(result, mapType);
//            for(Map.Entry<String,Map<String,ArrayList<Object>>> tlE : currenciesmaps.entrySet()) 
//                System.out.println(tlE.getValue());
//        }
//        
        
        Map<String,String> currenciesmapsMap = new HashMap<String,String>();
//        for(Map.Entry<String,PoEdotcomCurrency> currencymap : currenciesmaps.getResult().getCurrency().entrySet())
//            currenciesmapsMap.put(currencymap.getValue().getId(), currencymap.getValue().getText());
        for(Map.Entry<String,Map<String,ArrayList<PoEdotcomIDText>>> tlE : currenciesmaps.entrySet()) 
            //System.out.println(tlE.getValue());       
            for(Map.Entry<String,ArrayList<PoEdotcomIDText>> currencies : tlE.getValue().entrySet())
                for (PoEdotcomIDText currency : currencies.getValue())
                    currenciesmapsMap.put(currency.getId(), currency.getText());
        
        return currenciesmapsMap;
    }
    
    // https://www.pathofexile.com/api/trade/data/items -> we can get item-types and most uniques from there!
    public JsonObject getPoEdotcomData(String httpPostString, boolean write2File,String filePath) throws IOException {
        //https://www.pathofexile.com/api/trade/data/items
        /////////////////
        // Send GET
        /////////////////
        HttpGet httpPost = new HttpGet(httpPostString);    
        CloseableHttpResponse response = null;
        response = httpclient.execute(target, httpPost);
            
        /////////////////
        // Get RESPONSE
        /////////////////
        HttpEntity entity = response.getEntity();
        String result = EntityUtils.toString(entity);
        logger.log(Level.DEBUG,result); 
//        System.out.println(result); 
        
        EntityUtils.consume(entity);

        gson = new Gson();
        //Type mapType = new TypeToken<CurrencyOverviewResult>() {}.getType();
        JsonParser jsonParser = new JsonParser();
        JsonReader reader = new JsonReader(new StringReader(result));
        JsonObject jsonObj = gson.fromJson (jsonParser.parse(reader), JsonElement.class).getAsJsonObject();   //do like this to parse anything (for example to beatufy, write to file and then model after the file :)
        //CurrencyOverviewResult exchangeRates = gson.fromJson(jsonParser.parse(reader), mapType);
        
        response.close();
        
        logger.log(Level.DEBUG,"################# now showing all data queried "
                + "from www.pathofexile.com" + httpPostString + " #################");
//        System.out.println("################# now showing all data queried "
//                + "from www.pathofexile.com" + httpPostString + " #################");
        //System.out.println(JSONBeautifier.fomat(modStats.toString()));
        String jsonString = JSONBeautifier.format(jsonObj.toString());
        logger.log(Level.DEBUG,jsonString);
        logger.log(Level.DEBUG,"################# done showing all data queried "
                + "from www.pathofexile.com" + httpPostString + " #################");
//        System.out.println(jsonString);
//        System.out.println("################# done showing all data queried "
//                + "from www.pathofexile.com" + httpPostString + " #################");
        
        if (write2File) {
            // poe.ninja/api/Data/GetCurrencyOverview?league=Betrayal
            PrintWriter out = new PrintWriter(getPath(filePath));
            out.println(jsonString);
            out.flush();
            out.close();
        }
        
        return(jsonObj);
    }      
    
    /* !!! TODO-3 !!! BULK EXCHANGE (of poe.com) 
        -> has other structure for request + probably response aswell 
        -> talks to the same server - /api/trade/fetch/ => BUT I'M NOT 100% on that!
        {
                "exchange":{
                        "status":{
                                "option":"online"
                        },
                        "have":["alt"],
                        "want":["chaos"]
                }
        }    
    */  
    public PoEdotcomOffers getBulkOffer(StatusValues status,String league, 
                        String accountName, String have, CurrencyAmount want) throws IOException { 
        
        PoEdotcomOffers offers = new PoEdotcomOffers(); //i think it's the same
        
        System.setProperty("file.encoding", "ASCII"); //probably useless
        Gson gson = new Gson();
        BulkQueryRequest myQuery = new BulkQueryRequest();
//        ArrayList<StatsFiltersField> statfilters = new ArrayList<StatsFiltersField>();
        myQuery.makeQuery(status, have, want, accountName);
        
        //DEBUG-PART for DEVELOPMENT start
        String jsonString = gson.toJson(myQuery);
        logger.log(Level.DEBUG,"##### now printing request #########################\n"
                + JSONBeautifier.format(jsonString) 
                + "\n##### end of request #########################");
//        System.out.println("##### now printing request #########################\n"
//                + JSONBeautifier.format(jsonString) 
//                + "\n##### end of request #########################");
        
        //exit(0);
        //DEBUG-PART for DEVELOPMENT end
        
        /////////////////
        // Send POST
        /////////////////
        //HttpPost httppost = new HttpPost("/api/trade/search/Standard");
        HttpPost httppost = new HttpPost("/api/trade/exchange/"+league.trim());
        StringEntity requestEntity = new StringEntity(
            jsonString,
            ContentType.APPLICATION_JSON);        
        httppost.setEntity(requestEntity);
        CloseableHttpResponse response = httpclient.execute(target, httppost);

        /////////////////
        // Get RESPONSE
        /////////////////
        try {
            HttpEntity entity = response.getEntity();
            String result = EntityUtils.toString(entity);
            logger.log(Level.DEBUG,result);
//            System.out.println(result);
            
            //getPoEdotcomStats();  //TODO: make a Button for this Method (update Mod-Data!)
            
            readResponse(result,offers,QueryType.exchange);
            EntityUtils.consume(entity);
            
        } finally {
            response.close();
            return offers;
        }            
    }
    public PoEdotcomOffers getMultiBulkOffer(StatusValues status,String league, 
                        String accountName, ArrayList<CurrencyAmount> have, CurrencyAmount want) throws IOException { 
        
        PoEdotcomOffers offers = new PoEdotcomOffers(); //i think it's the same
        
        System.setProperty("file.encoding", "ASCII"); //probably useless
        Gson gson = new Gson();
        BulkQueryRequest myQuery = new BulkQueryRequest();
//        ArrayList<StatsFiltersField> statfilters = new ArrayList<StatsFiltersField>();
        ArrayList<String> haveStr = new ArrayList<String>();
        for(CurrencyAmount curr : have) 
            haveStr.add(curr.getType());
        myQuery.makeMultiQuery(status, haveStr, want, accountName);
        
        //DEBUG-PART for DEVELOPMENT start
        String jsonString = gson.toJson(myQuery);
        logger.log(Level.DEBUG,"##### now printing request #########################\n"
                + JSONBeautifier.format(jsonString) 
                + "\n##### end of request #########################");
//        System.out.println("##### now printing request #########################\n"
//                + JSONBeautifier.format(jsonString) 
//                + "\n##### end of request #########################");
        
        //exit(0);
        //DEBUG-PART for DEVELOPMENT end
        
        /////////////////
        // Send POST
        /////////////////
        //HttpPost httppost = new HttpPost("/api/trade/search/Standard");
        HttpPost httppost = new HttpPost("/api/trade/exchange/"+league.trim());
        StringEntity requestEntity = new StringEntity(
            jsonString,
            ContentType.APPLICATION_JSON);        
        httppost.setEntity(requestEntity);
        CloseableHttpResponse response = httpclient.execute(target, httppost);

        /////////////////
        // Get RESPONSE
        /////////////////
        try {
            HttpEntity entity = response.getEntity();
            String result = EntityUtils.toString(entity);
            logger.log(Level.DEBUG,result);
//            System.out.println(result);
            
            //getPoEdotcomStats();  //TODO: make a Button for this Method (update Mod-Data!)
            
            readResponse(result,offers,QueryType.exchange);
            EntityUtils.consume(entity);
            
        } finally {
            response.close();
            return offers;
        }            
    }
    public PoEdotcomOffers getBundledBulkOffers(StatusValues status,String league, 
                        String accountName, ArrayList<CurrencyAmount> have, ArrayList<CurrencyAmount> want) throws IOException { 
        
        PoEdotcomOffers offers = new PoEdotcomOffers(); //i think it's the same
        
        System.setProperty("file.encoding", "ASCII"); //probably useless
        Gson gson = new Gson();
        BulkQueryRequest myQuery = new BulkQueryRequest();
//        ArrayList<StatsFiltersField> statfilters = new ArrayList<StatsFiltersField>();
        ArrayList<String> haveStr = new ArrayList<String>();
        for(CurrencyAmount curr : have) 
            haveStr.add(curr.getType());
        myQuery.makeMultiQuery(status, haveStr, want, accountName);
        
        //DEBUG-PART for DEVELOPMENT start
        String jsonString = gson.toJson(myQuery);
        logger.log(Level.DEBUG,"##### now printing request #########################\n"
                + JSONBeautifier.format(jsonString) 
                + "\n##### end of request #########################");
//        System.out.println("##### now printing request #########################\n"
//                + JSONBeautifier.format(jsonString) 
//                + "\n##### end of request #########################");
        
        //exit(0);
        //DEBUG-PART for DEVELOPMENT end
        
        /////////////////
        // Send POST
        /////////////////
        //HttpPost httppost = new HttpPost("/api/trade/search/Standard");
        HttpPost httppost = new HttpPost("/api/trade/exchange/"+league.trim());
        StringEntity requestEntity = new StringEntity(
            jsonString,
            ContentType.APPLICATION_JSON);        
        httppost.setEntity(requestEntity);
        CloseableHttpResponse response = httpclient.execute(target, httppost);

        /////////////////
        // Get RESPONSE
        /////////////////
        try {
            HttpEntity entity = response.getEntity();
            String result = EntityUtils.toString(entity);
            logger.log(Level.DEBUG,result);
//            System.out.println(result);
            
            //getPoEdotcomStats();  //TODO: make a Button for this Method (update Mod-Data!)
            
            readResponse(result,offers,QueryType.exchange);
            EntityUtils.consume(entity);
            
        } finally {
            response.close();
            return offers;
        }            
    }
    public PoEdotcomOffers getOffersByTypeOnly(StatusValues status,String league, QueryType queryType, String accountName, String itemType) throws IOException {
        //return(getOffers(status,league, queryType, accountName, "", itemType));
        return(getOffers(status,league, queryType, accountName, "", itemType));
    }
    public PoEdotcomOffers getOffers(StatusValues status,String league, QueryType queryType, String accountName, String itemName, String itemType) throws IOException { 
    //        String stashName, byte stashX, byte stashY) throws IOException {  
        //needed parameters:                                                    
        //|<-----TradeFilters----->|<-query->|<------(probably)TradeFilters------>|   
        // accountname/lastCharName, itemname, location in stash(x+y-coords)+stash
        //price (but this only once we have the results - probably not here, 
        //but in a different class (e.g. only return the real price from this function
        //and the comparing is done by the caller!
        // => official API cannot filter for stash (and classes should only know what they need to know!)
        //  so we'll compare the matching (in accountName + itemName matching) in the caller-class 
        if (queryType == QueryType.exchange)
            ;//!!TODO!! implement bulk-query (see JSON-structure at the end of this class)

        PoEdotcomOffers offers = new PoEdotcomOffers();
        
        System.setProperty("file.encoding", "ASCII"); //probably useless
        Gson gson = new Gson();
        QueryRequest myQuery = new QueryRequest();
        
//        ArrayList<StatsFiltersField> statfilters = new ArrayList<StatsFiltersField>();
    
        FiltersField filters = new FiltersField();
        TradeField tradefilters = new TradeField();
        
        //most likely not needed, for this feature at least => kept for now! TODO?
        //if (queryType != QueryType.currency)
        //    tradefilters.addTradeFilter(TradeField.Type.sale_type, "priced");
        
        //the following 5 lines are not needed for this method(price verification)
//        PriceRangeField priceRange = new PriceRangeField();
//        priceRange.setOption("exa");
//        priceRange.setMin(50);
//        priceRange.setMax(300);
//        tradefilters.addTradeFilter(TradeField.Type.price, priceRange);
        tradefilters.addTradeFilter(TradeField.Type.account, accountName.trim());
        filters.setTrade_filters(tradefilters);   
        
//        TypeField typefilters = new TypeField();
//        typefilters.addTypeFilter(TypeField.Type.category, itemType);
//        typefilters.addTypeFilter(TypeField.Type.rarity, Rarity.rare);
//        filters.setType_filters(typefilters);        
        
        if (queryType == QueryType.mapitem) {
            filters.setMisc_filters(mapMiscfilters);  
        }
        
        if ( (queryType == QueryType.item) && ((itemName.equals("")) || (itemName == null)) )
            myQuery.makeTypeOnlyQuery(status,itemType.trim(), filters, QueryRequest.Order.asc); 
        else if ((queryType == QueryType.currency) || (queryType == QueryType.mapitem))
            myQuery.makeCurrencyQuery(status,itemName.trim(), filters, QueryRequest.Order.asc); 
            //myQuery.makeCurrencyQuery(status,itemName.trim(), QueryRequest.Order.asc);
        
        //DEBUG-PART for DEVELOPMENT start
        String jsonString = gson.toJson(myQuery);
        logger.log(Level.DEBUG,"##### now printing request #########################\n"
                + JSONBeautifier.format(jsonString) 
                + "\n##### end of request #########################");
//        System.out.println("##### now printing request #########################\n"
//                + JSONBeautifier.format(jsonString) 
//                + "\n##### end of request #########################");
        
        //exit(0);
        //DEBUG-PART for DEVELOPMENT end
        
        /////////////////
        // Send POST
        /////////////////
        //HttpPost httppost = new HttpPost("/api/trade/search/Standard");
        HttpPost httppost = new HttpPost("/api/trade/search/"+league.trim());
        StringEntity requestEntity = new StringEntity(
            jsonString,
            ContentType.APPLICATION_JSON);        
        httppost.setEntity(requestEntity);
        CloseableHttpResponse response = httpclient.execute(target, httppost);

        /////////////////
        // Get RESPONSE
        /////////////////
        try {
            HttpEntity entity = response.getEntity();
            String result = EntityUtils.toString(entity);
//            System.out.println(result);
            logger.log(Level.DEBUG,result);
            
            //getPoEdotcomStats();  //TODO: make a Button for this Method (update Mod-Data!)
            
            readResponse(result,offers,queryType);
            EntityUtils.consume(entity);
            
        } finally {
            response.close();
            return offers;
        }            
    }
    /* !!! TODO !!! normal currency trade in regular trade section (of poe.com) 
            -> currencytype/-name goes to type field, everythign else stays the same!
    {
	"query":{
		"status":{
			"option":"online"
		},
		"type":"Orb of Alchemy",
		"stats":[{
			"type":"and",
			"filters":[]
		}]},
		"sort":{
			"price":"asc"
		}
    }
    */    
}
