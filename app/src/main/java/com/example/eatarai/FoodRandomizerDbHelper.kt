package com.example.eatarai

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.eatarai.data.HistoryRecord

class FoodRandomizerDbHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        const val DATABASE_NAME = "FoodRandomizer.db"
        const val DATABASE_VERSION = 7
    }

    override fun onCreate(db: SQLiteDatabase) {
        // Create Cuisine and Menu tables
        db.execSQL("PRAGMA encoding = 'UTF-8';")
        db.execSQL(
            "CREATE TABLE Cuisine (" +
                    "id INTEGER PRIMARY KEY, " +
                    "name TEXT, " +
                    "category TEXT)"
        )
        db.execSQL(
            "CREATE TABLE Menu (" +
                    "id INTEGER PRIMARY KEY, " +
                    "name TEXT, cuisine_id INTEGER, " +
                    "FOREIGN KEY(cuisine_id) REFERENCES Cuisine(id))"
        )
        db.execSQL(
            " CREATE TABLE History (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "cuisine TEXT,menu TEXT, category TEXT" +
                "timestamp DATETIME DEFAULT CURRENT_TIMESTAMP)"
        )

        // Insert sample data
        insertSampleData(db)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        if (oldVersion < newVersion) {
        db.execSQL("DROP TABLE IF EXISTS History")
        db.execSQL("DROP TABLE IF EXISTS Menu")
        db.execSQL("DROP TABLE IF EXISTS Cuisine")
        onCreate(db)
        }
    }


    private fun insertSampleData(db: SQLiteDatabase) {
        // Insert Cuisines with Categories (Food, Dessert)
        val cuisines = listOf(
            // Food Cuisines
            "Thai Food" to "Food",
            "Japanese Food" to "Food",
            "Korean" to "Food",
            "Chinese" to "Food",
            "Italian" to "Food",
            "American" to "Food",
            "Vietnamese" to "Food",
            "Indian" to "Food",
            "Mexican" to "Food",
            "South American" to "Food",

            // Dessert Cuisines (Thai and Others)
            "Thai Desserts" to "Dessert",
            "Japanese Desserts" to "Dessert",
            "Cakes" to "Dessert",
            "Ice Cream" to "Dessert",
            "Pudding" to "Dessert",
            "Pastries" to "Dessert",
            "Cookies" to "Dessert",
            "Drinks" to "Dessert",
            "Fruits" to "Dessert"
        )

        cuisines.forEach { (name, category) ->
            val values = ContentValues().apply {
                put("name", name)
                put("category", category)
            }
            db.insert("Cuisine", null, values)
        }

        val thaiFoodMenus = listOf(
            "ผัดไทย", "ต้มยำกุ้ง", "แกงเขียวหวาน", "แกงมัสมั่น", "ส้มตำ", "ข้าวผัด", "ผัดกระเพรา", "ข้าวซอย", "หมูปิ้ง", "ไก่ทอด", "ไส้อั่ว", "ห่อหมก", "ลาบ",
            "น้ำตก", "ปลาเผา", "แกงส้ม", "ยำวุ้นเส้น", "ทอดมันปลา", "ก๊วยเตี๋ยว", "ข้าวมันไก่", "ปลานึ่งมะนาว", "ผัดซีอิ๊ว", "ข้าวต้ม", "ข้าวหมกไก่", "ขนมจีนน้ำยา",
            "ผัดหมี่โคราช", "ยำปลาดุกฟู", "ไก่ย่าง", "ก๋วยเตี๋ยวเรือ", "ข้าวคลุกกะปิ", "พล่า", "ขนมจีนน้ำเงี้ยว", "กุ้งอบวุ้นเส้น", "ยำถั่วพู", "ข้าวหน้าเป็ด", "พริกขิง",
            "ยำไข่ดาว", "แกงป่า", "ไข่เจียวปู", "เนื้อแดดเดียว", "หมูแดดเดียว", "ยำมะม่วง","ผัดพริกเกลือ", "กะหล่ำปลีผัดน้ำปลา", "ข้าวยำปักษ์ใต้", "แกงคั่วหอยขม",
            "ต้มแซ่บกระดูกหมู", "ก๋วยจั๊บญวน", "ข้าวหน้าหมูแดง", "ผัดพริกแกง", "ผัดกระเทียม", "ข้าวขาหมู","ต้มข่า", "ผัดขี้เมา", "คะน้าน้ำมันหอย","คะน้าปลาเค็ม",
            "ผัดฉ่า", "แกงแดง", "คอหมูย่าง", "พะเแนง", "แกงหน่อไม้", "ผัดหน่อไม้", "ผัดหอยลาย", "หอยทอด", "ยำคอหมูย่าง", "ขนมปังหน้ากุ้ง", "ไข่ลูกเขย",
            "ผัดเม็ดมะม่วงหิมพานต์", "ผัดผัก", "ต้มเล้ง", "ปลาลุยสวน", "ปลาราดพริก", "ปลาทอดซอสมะขาม", "ข้าวผัดสัปปะรด", "ข้าวผัดรถไฟ", "ผัดพริกเกลือ",
            "ไก่ต้มใบมะขาม", "หมูฝอย", "ผัดผักบุ้ง", "ปูนิ่มผัดผงกะหรี่", "ปูนิ่มทอดกระเทียม", "ข้าวผัดต้มยำ", "สุกี้แห้ง", "สุกี้น้ำ", "หมูหวาน", "หมูคลุกฝุ่น", "ปลากระพงทอดน้ำปลา", "กุ้งแช่น้ำปลา",
            "เย็นตาโฟ", "ยำสามกรอบ", "ยำแซลมอน", "หมูกรอบ", "ไส้กรอกอีสาน", "แกงส้มแป๊ะซะ", "หมูกะทะ", "จิ้มจุ่ม", "โจ๊ก", "ก๊วยเตี๋ยวต้มยำ", "ตำข้าวโพด",
            "ปลาหมึกผัดไข่เค็ม", "น้ำตก", "ตับหวาน", "แหนมคลุก", "เสือร้องไห้ย่าง", "ปลาหมึกผัดผงกะหรี่", "กุ้งทอดกระเทียม", "ตำเส้นเล็ก", "สามชั้นทอดน้ำปลา"
        )

        val thaiDesserts = listOf(
            "ข้าวเหนียวมะม่วง", "ข้าวเหนียวทุเรียน", "ลอดช่อง", "ขนมครก", "ทับทิมกรอบ", "บัวลอยน้ำขิง", "ทองหยอด", "ฝอยทอง", "ขนมชั้น", "ข้าวต้มมัด", "ขนมตาล",
            "ขนมถ้วย", "สังขยาฟักทอง", "ขนมกล้วย", "ขนมเปียกปูน", "ข้าวเหนียวดำ", "วุ้นกะทิ", "ไอศกรีมกะทิ", "รวมมิตรน้ำกะทิ", "เครป", "โรตีสายไหม", "ข้าวเหนียวสังขยา", "เต้าฮวย",
            "ฟักทองเชื่อม", "เผือกกวน", "ขนมปังนมสด", "ขนมถังแตก", "ข้าวโพดคลุกเนย", "ทองหยิบ", "เม็ดขนุน", "กล้วยทอด", "สาคู", "เฉาก๊วยนมสด", "ขนมปังปิ้ง", "ขนมปังนึ่ง",
            "ครองแครง", "ข้าวเหนียวเปียกลำไย", "บ้าบิ่นมะพร้าวอ่อน", "ปังเย็น", "ถั่วแปบ", "ไข่นกกระทาทอด", "กล้วยบวชชี", "ขนมต้ม", "หม้อแกง", "ลูกชุบ", "ขนมอินทนิล", "ขนมใส่ไส้", "ตะโก้",
            "กล้วยเชื่อม", "มันเชื่อม", "ข้าวหลาม", "ขนมถ้วยฟู", "ขนมสาลี่", "ดอกจอก", "หยกมณี", "ฟักทองแกงบวด", "ลูกตาลลอยแก้ว", "ทองม้วน", "กล้วยทับ", "ปลากริมไข่เต่า",
            "ขนมเปี๊ยะ", "ซ่าหริ่ม", "ข้าวแต๋น", "ขนมไข่", "น้ำแข็งไส"
        )
        val japaneseFood = listOf(
            "Sushi", "Nabe", "Tempura", "Gyudon", "Oyakodon", "Tendon", "Unadon", "Tekkadon",
            "Udon", "Soba", "Tonkatsu", "Miso Soup", "Takoyaki", "Yakiniku", "Okonomiyaki",
            "Unagi", "Salmon", "Maguro", "Natto", "Tamagoyaki", "Sukiyaki", "Yakitori",
            "Shabu", "Curry Rice", "Onigiri", "Gyoza", "Yakisoba", "Mentaiko",
            "Tonkotsu Ramen", "Miso Ramen", "Shoyu Ramen", "Omurice", "Chasuke", "Temaki",
            "Inari Sushi", "Chirashi", "Oden Nabe", "Katsudon", "Uni", "Ikura",
        )
        val japaneseDesserts = listOf(
            "Mochi", "Dorayaki", "Taiyaki", "Dango", "Daifuku", "Matcha Ice Cream",
            "Yokan", "Anmitsu", "Warabi Mochi", "Kakigori", "Melonpan",
            "Castella Cake", "Souffle Pancake", "Cream Daifuku",
            "Purin", "Cheescake", "Tiramisu", "Crepe", "Matcha Bronwnie",
            "Green Tea Cheesecake", "Sakura Mochi ", "Water Drop Mochi",
            "Cheese Tart", "Matcha Pudding", "Strawberry Rollcake",
            "Strawberry Cake", "Sweet Potato", "Mochi Ice Cream"
        )
        // Insert Menus for Each Cuisine
        val menus = listOf(
            // Thai Food
            *thaiFoodMenus.map { it to "Thai Food" }.toTypedArray(),
            // Thai Desserts
            *thaiDesserts.map { it to "Thai Desserts" }.toTypedArray(),

            // Japanese Food
            *japaneseFood.map { it to "Japanese Food" }.toTypedArray(),
            //Japanese Desserts
            *japaneseDesserts.map { it to "Japanese Desserts" }.toTypedArray(),

            // Korean Food
            "Kimchi" to "Korean",
            "Tteokbokki" to "Korean",
            "Bibimbap" to "Korean",
            "Samgyeopsal" to "Korean",
            "Japchae" to "Korean",
            "Korean Fried Chicken" to "Korean",
            "Kimchi Stew" to "Korean",
            "Bulgogi" to "Korean",
            "Jjajangmyeon" to "Korean",
            "Hotteok" to "Korean",
            "Jjigae" to "Korean",
            "Ramyeon" to "Korean",
            "Korean Fried Chicken" to "Korean",
            "Samgyetang" to "Korean",
            "Hangover Stew" to "Korean",
            "Jjambbong" to "Korean",
            "Galbi" to "Korean",

            // Chinese Food
            "Sweet and Sour Pork" to "Chinese",
            "Dumplings" to "Chinese",
            "Peking Duck" to "Chinese",
            "Kung Pao Chicken" to "Chinese",
            "Hot Pot" to "Chinese",
            "Fried Rice" to "Chinese",
            "Chow Mein" to "Chinese",
            "Mapo Tofu" to "Chinese",
            "Spring Rolls" to "Chinese",
            "Char Siu" to "Chinese",
            "Mala" to "Chinese",
            "Dim Sum" to "Chinese",
            "Sichuan Pork" to "Chinese",
            "Xiaonlongbao" to "Chinese",
            "Zhajiangmian" to "Chinese",
            "Wonton" to "Chinese",

            // Italian Food
            "Hawaiian Pizza" to "Italian",
            "Pizza Napoletana" to "Italian",
            "Magherita Pizza" to "Italian",
            "Four Cheese Pizza" to "Italian",
            "Parma Ham Pizza" to "Italian",
            "Salami" to "Italian",
            "Lasagna" to "Italian",
            "Risotto" to "Italian",
            "Bruschetta" to "Italian",
            "Gnocchi" to "Italian",
            "Minestrone Soup" to "Italian",
            "Milanese" to "Italian",
            "Steak" to "Italian",
            "Parma Ham" to "Italian",
            "Carbonara Pasta" to "Italian",
            "Bolognese Pasta" to "Italian",
            "Pesto Pasta" to "Italian",
            "Aglio e Olio" to "Italian",

            // American Food
            "Cheeseburger" to "American",
            "Fried Chicken" to "American",
            "Hot Dog" to "American",
            "BBQ Ribs" to "American",
            "Mac and Cheese" to "American",
            "Clam Chowder" to "American",
            "Cornbread" to "American",
            "Buffalo Wings" to "American",
            "Steak" to "American",
            "Philly Cheesesteak" to "American",
            "Chicken and Waffles" to "American",
            "Pot Roast" to "American",
            "Meatloaf" to "American",
            "Pulled Pork Sandwich" to "American",
            "Jambalaya" to "American",
            "Lobster Roll" to "American",
            "Grilled Cheese" to "American",
            "Reuben Sandwich" to "American",
            "Pecan Pie" to "American",
            "Apple Pie" to "American",

            // Vietnamese Food
            "Pho" to "Vietnamese",
            "Banh Mi" to "Vietnamese",
            "Spring Rolls" to "Vietnamese",
            "Bun Cha" to "Vietnamese",
            "Cao Lau" to "Vietnamese",
            "Bo Kho" to "Vietnamese",
            "Goi Cuon" to "Vietnamese",
            "Com Tam" to "Vietnamese",

            // Indian Food
            "Chicken Tikka Masala" to "Indian",
            "Butter Chicken" to "Indian",
            "Rogan Josh" to "Indian",
            "Biryani" to "Indian",
            "Palak Paneer" to "Indian",
            "Naan" to "Indian",
            "Samosa" to "Indian",
            "Dosa" to "Indian",
            "Chai Tea" to "Indian",

            // Mexican Food
            "Tacos" to "Mexican",
            "Burritos" to "Mexican",
            "Enchiladas" to "Mexican",
            "Quesadillas" to "Mexican",
            "Churros" to "Mexican",
            "Tamales" to "Mexican",
            "Guacamole" to "Mexican",
            "Nachos" to "Mexican",
            "Fajitas" to "Mexican",

            // South American Food
            "Feijoada" to "South American",
            "Pão de Queijo" to "South American",
            "Chimichurri Steak" to "South American",
            "Asado" to "South American",
            "Empanadas" to "South American",
            "Moqueca (Seafood Stew)" to "South American",
            "Açaí Bowl" to "South American",

            // Pudding
            "Chocolate Pudding" to "Pudding",
            "Vanilla Pudding" to "Pudding",
            "Sticky Toffee Pudding" to "Pudding",
            "Rice Pudding" to "Pudding",
            "Banana Pudding" to "Pudding",
            "Fresh Coconut Pudding" to "Pudding",
            "Custard" to "Pudding",
            "Fruit Pudding" to "Pudding",
            "Milk Pudding" to "Pudding",

            // Cookies
            "Chocolate Chip Cookie" to "Cookies",
            "Oatmeal Raisin Cookie" to "Cookies",
            "Sugar Cookie" to "Cookies",
            "Gingerbread Cookie" to "Cookies",
            "Shortbread Cookie" to "Cookies",
            "Peanut Butter Cookie" to "Cookies",
            "Double Chocolate Cookie" to "Cookies",
            "Snickerdoodle" to "Cookies",
            "Macadamia Nut Cookie" to "Cookies",
            "White Chocolate Macadamia Cookie" to "Cookies",
            "Red Velvet Cookie" to "Cookies",
            "Almond Biscotti" to "Cookies",
            "Butter Cookie" to "Cookies",
            "Lemon Drop Cookie" to "Cookies",
            "Coconut Macaroon" to "Cookies",

            // Pastries
            "Croissant" to "Pastries",
            "Pain au Chocolat" to "Pastries",
            "Danish Pastry" to "Pastries",
            "Apple Turnover" to "Pastries",
            "Cinnamon Roll" to "Pastries",
            "Eclair" to "Pastries",
            "Macaron" to "Pastries",
            "Alfajor" to "Pastries",
            "Curry Puff" to "Pastries",
            "Donut" to "Pastries",
            "Brownie" to "Pastries",
            "Taiyaki" to "Pastries",
            "Almond Croissant" to "Pastries",
            "Cinnamon Bun" to "Pastries",
            "Soho Bun" to "Pastries",
            "Choux" to "Pastries",

            // Cakes
            "Matcha Cake" to "Cakes",
            "Tiramisu" to "Cakes",
            "Cheesecake" to "Cakes",
            "Chocolate Lava Cake" to "Cakes",
            "Victoria Sponge" to "Cakes",
            "Crepe Cake" to "Cakes",
            "Strawberry Cake" to "Cakes",
            "Lemon Cake" to "Cakes",
            "Carrot Cake" to "Cakes",
            "Toffee Cake" to "Cakes",
            "Strawberry Shortcake" to "Cakes",
            "Blueberry Cheesecake" to "Cakes",
            "Fresh Coconut Cake" to "Cakes",

            // Ice Cream
            "Mochi Ice Cream" to "Ice Cream",
            "Vanilla Ice Cream" to "Ice Cream",
            "Chocolate Ice Cream" to "Ice Cream",
            "Strawberry Sorbet" to "Ice Cream",
            "Lemon Sherbet Ice Cream" to "Ice Cream",
            "Milk Ice Cream" to "Ice Cream",
            "Coconut Ice Cream" to "Ice Cream",
            "Cookie&Cream Sorbet" to "Ice Cream",
            "Chocolate Chip Ice Cream" to "Ice Cream",
            "Matcha Ice Cream" to "Ice Cream",
            "Thai Tee Ice Cream" to "Ice Cream",
            "Blueberry Sorbet" to "Ice Cream",

            // Drinks
            "Bubble Tea" to "Drinks",
            "Thai Iced Tea" to "Drinks",
            "Mango Smoothie" to "Drinks",
            "Strawberry Milkshake" to "Drinks",
            "Chocolate Milkshake" to "Drinks",
            "Iced Coffee with Caramel" to "Drinks",
            "Honey Lemonade" to "Drinks",
            "Pineapple Juice" to "Drinks",
            "Watermelon Juice" to "Drinks",
            "Coconut Water with Sugar" to "Drinks",
            "Peach Iced Tea" to "Drinks",
            "Iced Matcha Latte with Honey" to "Drinks",
            "Lychee Juice" to "Drinks",
            "Passion Fruit Smoothie" to "Drinks",
            "Blueberry Lemonade" to "Drinks",
            "Mint Mojito Mocktail" to "Drinks",
            "Raspberry Iced Tea" to "Drinks",
            "Cherry Limeade" to "Drinks",
            "Rose Milk" to "Drinks",
            "Pomegranate Juice" to "Drinks",
            "Vanilla Bean Frappuccino" to "Drinks",
            "Cream Soda Float" to "Drinks",
            "Orange Creamsicle" to "Drinks",
            "Apple Cider (Cold)" to "Drinks",
            "Sweetened Green Tea" to "Drinks",
            "Melon Soda" to "Drinks",
            "Grapefruit Honey Soda" to "Drinks",
            "Banana Smoothie" to "Drinks",
            "Strawberry Lemonade" to "Drinks",

            // Fruits
            "Apple" to "Fruits",
            "Banana" to "Fruits",
            "Orange" to "Fruits",
            "Strawberry" to "Fruits",
            "Pineapple" to "Fruits",
            "Grape" to "Fruits",
            "Watermelon" to "Fruits",
            "Cherry" to "Fruits",
            "Blueberry" to "Fruits",
            "Mango" to "Fruits",
            "Kiwi" to "Fruits",
            "Lemon" to "Fruits",
            "Lime" to "Fruits",
            "Peach" to "Fruits",
            "Plum" to "Fruits",
            "Pear" to "Fruits",
            "Pomegranate" to "Fruits",
            "Raspberry" to "Fruits",
            "Blackberry" to "Fruits",
            "Cantaloupe" to "Fruits",
            "Papaya" to "Fruits",
            "Grapefruit" to "Fruits",
            "Dragon Fruit" to "Fruits",
            "Lychee" to "Fruits",
            "Coconut" to "Fruits",
            "Avocado" to "Fruits",
            "Guava" to "Fruits",
            "Passion Fruit" to "Fruits",
        )


        menus.forEach { (menu, cuisine) ->
            val cuisineId = getCuisineIdByName(db, cuisine)
            val values = ContentValues().apply {
                put("name", menu)
                put("cuisine_id", cuisineId)
            }
            db.insert("Menu", null, values)
        }
    }

    private fun getCuisineIdByName(db: SQLiteDatabase, name: String): Int {
        val cursor = db.rawQuery("SELECT id FROM Cuisine WHERE name = ?", arrayOf(name))
        val id = if (cursor.moveToFirst()) cursor.getInt(0) else -1
        cursor.close()
        return id
    }

    fun getRandomCuisine(isFood: Boolean, cuisine: String? = null): String? {
        val foodOrDessert = if (isFood) "Food" else "Dessert"
        val db = readableDatabase

        // Dynamically adjust the query and arguments based on the 'cuisine' parameter.
        val query = if (cuisine != null) {
            "SELECT name FROM Cuisine WHERE category = ? AND name = ? ORDER BY RANDOM() LIMIT 1"
        } else {
            "SELECT name FROM Cuisine WHERE category = ? ORDER BY RANDOM() LIMIT 1"
        }

        val args = if (cuisine != null) arrayOf(foodOrDessert, cuisine) else arrayOf(foodOrDessert)

        val cursor: Cursor = db.rawQuery(query, args)

        val cuisineName = if (cursor.moveToFirst()) cursor.getString(0) else null
        cursor.close()
        return cuisineName
    }


    fun getRandomMenu(
        cuisines: List<String> = listOf(),
        isFood: Boolean,
        includeOthers: Boolean = false
    ): Pair<String?, String?>? {
        val db = readableDatabase

        Log.d(
            "FoodRandomizerDbHelper",
            "getRandomMenu called with cuisines: $cuisines, isFood: $isFood, includeOthers: $includeOthers"
        )

        val excludedCuisines = if (isFood) {
            listOf(
                "Japanese Food", "Korean", "Italian", "Chinese", "Thai Food"
            )
        } else {
            listOf("Cakes", "Ice Cream", "Pastries", "Fruit", "Cookies")
        }

        Log.d("FoodRandomizerDbHelper", "Excluded cuisines: $excludedCuisines")

        val query = if (includeOthers) {
            """
            SELECT Menu.name, Cuisine.name as cuisine
            FROM Menu
            JOIN Cuisine ON Menu.cuisine_id = Cuisine.id
            WHERE Cuisine.category = ? AND Cuisine.name NOT IN (${excludedCuisines.joinToString(",") { "?" }})
            ORDER BY RANDOM()
            LIMIT 1
            """
        } else {
            """
            SELECT Menu.name, Cuisine.name as cuisine
            FROM Menu
            JOIN Cuisine ON Menu.cuisine_id = Cuisine.id
            WHERE Cuisine.name IN (${cuisines.joinToString(",") { "?" }})
            ORDER BY RANDOM()
            LIMIT 1
            """
        }

        Log.d("FoodRandomizerDbHelper", "SQL Query: $query")

        // Set up arguments for the query
        val args: Array<String> = if (includeOthers) {
            arrayOf(if (isFood) "Food" else "Dessert") + excludedCuisines.toTypedArray()
        } else {
            cuisines.toTypedArray()
        }

        val cursor = db.rawQuery(query, args)
        val result = if (cursor.moveToFirst()) {
            cursor.getString(cursor.getColumnIndexOrThrow("name")) to
                    cursor.getString(cursor.getColumnIndexOrThrow("cuisine"))
        } else null

        cursor.close()
        return result
    }

    // Add a function to insert history records
    fun addHistoryRecord(cuisine: String, menu: String, category: String) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("cuisine", cuisine)  // Use actual cuisine
            put("menu", menu)
            put("category", category)
        }
        db.insert("History", null, values)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getHistoryRecords(): List<HistoryRecord> {
        val db = readableDatabase
        val cursor = db.rawQuery(
            "SELECT cuisine, menu, category, timestamp FROM History ORDER BY timestamp DESC",
            null
        )

        val historyRecords = mutableListOf<HistoryRecord>()
        with(cursor) {
            while (moveToNext()) {
                val cuisine = getString(getColumnIndexOrThrow("cuisine")) ?: "Unknown Cuisine"
                val menu = getString(getColumnIndexOrThrow("menu")) ?: "Unknown Menu"
                val category = getString(getColumnIndexOrThrow("category")) ?: "Unknown Category"
                val timestamp = getString(getColumnIndexOrThrow("timestamp")) ?: "Unknown Date"

                historyRecords.add(HistoryRecord(cuisine, menu, category, timestamp))
            }
            close()
        }
        return historyRecords
    }

    fun clearHistory() {
        val db = writableDatabase
        db.beginTransaction()
        try {
            db.delete("History", null, null)  // Delete all records from History table
            db.execSQL("DELETE FROM sqlite_sequence WHERE name = 'History';")  // Reset auto-increment
            db.setTransactionSuccessful()
        } finally {
            db.endTransaction()
        }
    }

    fun deleteHistoryRecord(menu: String) {
        val db = writableDatabase
        db.delete("History", "menu = ?", arrayOf(menu))
    }
}
