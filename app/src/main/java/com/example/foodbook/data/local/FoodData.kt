package com.example.foodbook.data.local

import com.example.foodbook.data.model.MealDetail

/**
 * 40 món ăn làm được tại nhà: mỗi trang (Việt, Âu, Nhật, Trung) có đúng 10 món.
 * wiki = tên bài Wikipedia của món đó, app lấy ảnh đại diện của bài viết làm ảnh món (đúng món).
 */
object FoodData {
    private class Dish(val meal: MealDetail, val wiki: String)

    private fun d(
        id: String, name: String, category: String, area: String, diet: String, wiki: String,
        ing: List<String>, steps: List<String>
    ) = Dish(
        MealDetail(
            id = id, name = name, thumb = "", category = category, area = area,
            instructions = steps.mapIndexed { i, s -> "${i + 1}. $s" }.joinToString("\n\n"),
            ingredients = ing, dietType = diet
        ), wiki
    )

    private const val VIET = "Món Việt"
    private const val AU = "Món Âu"
    private const val NHAT = "Món Nhật"
    private const val TRUNG = "Món Trung"

    private val dishes = listOf(
        // ============================ MÓN VIỆT ============================
        d("v01", "Phở bò", VIET, "Miền Bắc", "thit", "Phở",
            listOf("500g bánh phở tươi", "1 kg xương ống bò", "400g thịt bò (nạm, tái)", "1 củ hành tây, 1 củ gừng (nướng)", "Hoa hồi, quế, thảo quả", "2 muỗng canh nước mắm, 1 muỗng canh đường phèn", "Hành lá, ngò, giá, chanh, ớt"),
            listOf("Chần xương bò qua nước sôi 5 phút, rửa sạch để nước dùng trong.", "Nướng hành tây và gừng cho thơm, rang hoa hồi, quế, thảo quả.", "Ninh xương với 3 lít nước cùng gia vị trên lửa nhỏ 3 đến 4 giờ, thường xuyên vớt bọt.", "Nêm nước mắm, đường phèn, muối cho vừa ăn; thái thịt bò mỏng.", "Chần bánh phở, xếp thịt và hành lá vào tô, chan nước dùng đang sôi; ăn kèm giá, chanh, ớt.")),
        d("v02", "Gà chiên mật ong", VIET, "Việt Nam", "thit", "Honey chicken",
            listOf("500g cánh gà hoặc đùi gà", "3 muỗng canh mật ong", "2 muỗng canh nước tương", "1 muỗng canh nước mắm", "4 tép tỏi băm", "1 muỗng cà phê tiêu", "Bột chiên giòn hoặc bột bắp", "Dầu ăn để chiên", "Mè rang"),
            listOf("Rửa sạch gà, để ráo; ướp với tỏi băm, nước tương, nước mắm, tiêu và 1 muỗng canh mật ong trong 30 phút.", "Lăn gà qua bột chiên giòn hoặc bột bắp cho phủ đều.", "Chiên gà ngập dầu lửa vừa 10 đến 12 phút đến khi vàng giòn, chín kỹ; vớt ra giấy thấm dầu.", "Làm sốt: đun 2 muỗng canh mật ong với 1 muỗng canh nước tương và chút tỏi băm đến khi sệt nhẹ.", "Cho gà vào chảo, đảo nhanh với sốt cho áo đều, rắc mè rang rồi dùng nóng.")),
        d("v03", "Cơm chiên Dương Châu", VIET, "Việt Nam", "thit", "Yangzhou fried rice",
            listOf("3 chén cơm nguội", "100g tôm bóc vỏ", "100g lạp xưởng hoặc xá xíu thái hạt lựu", "2 quả trứng", "Cà rốt, đậu Hà Lan, bắp hạt", "2 củ hành lá", "1 muỗng canh nước tương", "Dầu ăn, muối, tiêu"),
            listOf("Dùng cơm nguội, tơi rời từng hạt; thái nhỏ lạp xưởng, cà rốt.", "Chiên trứng đánh tan trên chảo nóng đến khi vừa chín rồi múc ra.", "Phi hành, xào tôm và lạp xưởng cho chín, thêm cà rốt, đậu Hà Lan, bắp hạt.", "Cho cơm vào, đảo đều tay trên lửa lớn đến khi hạt cơm khô, săn.", "Thêm trứng, nêm nước tương, muối, tiêu rồi rắc hành lá, tắt bếp.")),
        d("v04", "Bún bò Huế", VIET, "Miền Trung", "thit", "Bún bò Huế",
            listOf("500g bắp bò, 500g giò heo", "500g xương heo", "5 cây sả", "2 muỗng canh mắm ruốc", "2 muỗng canh sa tế, 1 muỗng canh ớt bột", "500g bún to", "Hành tây, hành lá, ngò", "Giá, rau muống chẻ, bắp chuối, chanh"),
            listOf("Chần xương và giò heo, rửa sạch; đập dập sả.", "Hầm xương, bắp bò, giò heo cùng sả khoảng 2 giờ; vớt thịt ngâm nước lạnh rồi thái lát.", "Hòa mắm ruốc với nước, để lắng lấy phần trong cho vào nồi.", "Phi sả với sa tế, ớt bột rồi cho vào nồi nước dùng, nêm cho vừa ăn.", "Chần bún, xếp thịt vào tô, chan nước dùng; ăn kèm rau sống và chanh.")),
        d("v05", "Mì Quảng", VIET, "Miền Trung", "thit", "Mì Quảng",
            listOf("300g mì Quảng (hoặc bánh phở tươi)", "200g tôm", "200g thịt ba chỉ", "Nghệ, hành tím, tỏi", "Đậu phộng rang, bánh tráng mè", "Rau sống, chuối bắp, giá", "Nước mắm, đường, tiêu"),
            listOf("Ướp tôm và thịt ba chỉ với nghệ, hành tỏi, nước mắm, tiêu.", "Phi thơm hành tỏi, xào thịt và tôm chín, đổ nước vừa ngập, đun nhỏ lửa cho hơi sệt.", "Chần mì qua nước sôi, cho vào tô, thêm ít rau sống ở dưới.", "Múc nhân và ít nước xâm xấp, rắc đậu phộng, bẻ bánh tráng mè lên trên.")),
        d("v06", "Thịt kho trứng", VIET, "Miền Nam", "thit", "Thịt kho",
            listOf("600g thịt ba chỉ", "6 quả trứng (luộc, bóc vỏ)", "500ml nước dừa tươi", "3 muỗng canh nước mắm", "2 muỗng canh đường", "Hành tím, tỏi, tiêu, ớt"),
            listOf("Thái thịt ba chỉ miếng vuông, ướp hành tỏi băm, nước mắm, tiêu 30 phút.", "Thắng đường làm nước màu đến vàng cánh gián.", "Cho thịt vào đảo săn, đổ nước dừa ngập thịt, đun sôi rồi hạ nhỏ lửa.", "Kho 40 phút, thêm trứng luộc, kho thêm 20 phút đến khi nước sệt, thịt mềm.", "Ăn nóng với cơm trắng và dưa chua.")),
        d("v07", "Canh chua cá", VIET, "Miền Nam", "hai_san", "Canh chua",
            listOf("500g cá lóc (hoặc cá basa)", "2 quả cà chua", "100g đậu bắp", "100g giá, 1/4 quả thơm", "30g me", "1 cây bạc hà", "Rau om, ngò gai, tỏi, ớt", "Nước mắm, đường"),
            listOf("Cá làm sạch, cắt khúc, ướp chút muối và tiêu. Hòa me với nước nóng rồi lọc lấy nước chua.", "Phi thơm tỏi, cho cà chua và thơm vào xào sơ.", "Đổ nước me vào, đun sôi rồi cho cá vào nấu chín; nêm nước mắm, đường cho chua ngọt hài hòa.", "Cho đậu bắp, bạc hà, giá vào nấu thêm 2 phút; tắt bếp, thêm rau om, ngò gai và ớt.")),
        d("v08", "Bánh xèo", VIET, "Miền Nam", "hai_san", "Bánh xèo",
            listOf("250g bột gạo, 1 muỗng cà phê nghệ", "400ml nước cốt dừa pha loãng", "Hành lá", "300g tôm, 300g thịt ba chỉ", "200g giá đỗ", "Rau sống, cải xanh", "Nước mắm chua ngọt"),
            listOf("Trộn bột gạo, nghệ, nước cốt dừa, hành lá, chút muối; để nghỉ 30 phút.", "Thịt ba chỉ thái mỏng, tôm bóc vỏ; xào sơ cho thơm.", "Làm nóng chảo, thoa ít dầu, đổ một vá bột, xoay đều cho mỏng; cho tôm, thịt, giá vào một nửa bánh.", "Đậy nắp 2 phút rồi mở nắp chiên giòn, gấp đôi bánh.", "Cuộn bánh với rau sống trong lá cải xanh, chấm nước mắm chua ngọt.")),
        d("v09", "Gỏi cuốn tôm thịt", VIET, "Miền Nam", "hai_san", "Gỏi cuốn",
            listOf("12 tờ bánh tráng", "300g tôm", "200g thịt ba chỉ", "200g bún tươi", "Xà lách, giá, rau thơm, hẹ", "3 muỗng canh tương hoisin", "2 muỗng canh đậu phộng rang giã nhỏ"),
            listOf("Luộc thịt ba chỉ chín rồi thái lát mỏng; luộc tôm, bóc vỏ và bổ đôi.", "Rửa sạch rau, để ráo; nhúng nhanh bánh tráng vào nước rồi trải lên thớt.", "Xếp xà lách, bún, rau thơm, thịt, tôm lên bánh tráng, cuốn chặt tay và gài vài cọng hẹ.", "Pha nước chấm từ tương hoisin, đậu phộng, tỏi ớt băm và chút nước ấm.")),
        d("v10", "Bò lúc lắc", VIET, "Miền Nam", "thit", "Shaking beef",
            listOf("400g thăn bò thái hạt lựu", "2 củ hành tây", "1 quả ớt chuông", "4 tép tỏi băm", "2 muỗng canh dầu hào", "1 muỗng canh nước tương, 1 muỗng canh đường", "Bơ, tiêu", "Xà lách, cà chua, dưa leo"),
            listOf("Ướp thịt với tỏi, dầu hào, nước tương, đường, tiêu 30 phút.", "Làm nóng chảo thật nóng với bơ, áp chảo thịt từng đợt, lắc chảo 1 đến 2 phút cho thịt săn, vàng.", "Xào nhanh hành tây và ớt chuông với chút dầu.", "Trộn tất cả, nêm nếm lại rồi tắt bếp.", "Bày thịt lên xà lách, cà chua, dưa leo; ăn kèm cơm hoặc bánh mì.")),

        // ============================ MÓN ÂU ============================
        d("a01", "Mì Ý Carbonara", AU, "Ý", "thit", "Carbonara",
            listOf("200g mì Ý spaghetti", "100g thịt xông khói (bacon) thái nhỏ", "2 lòng đỏ trứng và 1 quả trứng", "50g phô mai Parmesan bào", "2 tép tỏi", "Tiêu đen xay, muối"),
            listOf("Luộc mì trong nước sôi có muối, giữ lại nửa chén nước luộc.", "Chiên bacon với tỏi đến khi giòn rồi tắt bếp.", "Trộn trứng, phô mai Parmesan và nhiều tiêu trong một tô.", "Cho mì vào chảo bacon (đã tắt bếp), đổ hỗn hợp trứng vào đảo nhanh, thêm chút nước luộc mì cho sốt mịn.", "Rắc thêm phô mai và tiêu, dùng ngay.")),
        d("a02", "Mì Ý sốt bò bằm", AU, "Ý", "thit", "Bolognese sauce",
            listOf("200g mì Ý", "300g thịt bò xay", "1 củ hành tây, 1 củ cà rốt, 2 tép tỏi", "400g cà chua xay hoặc sốt cà chua", "2 muỗng canh tương cà", "Húng tây, muối, tiêu", "Phô mai Parmesan"),
            listOf("Phi thơm hành, tỏi, cà rốt băm nhỏ rồi cho thịt bò xay vào xào săn.", "Thêm cà chua, tương cà, húng tây, muối, tiêu; ninh lửa nhỏ 30 đến 40 phút.", "Luộc mì chín tới, vớt ráo.", "Cho mì ra đĩa, rưới sốt bò và rắc phô mai Parmesan.")),
        d("a03", "Lasagne", AU, "Ý", "thit", "Lasagne",
            listOf("9 lá mì lasagne", "400g sốt bò bằm", "500ml sốt béchamel (bơ, bột mì, sữa)", "200g phô mai Mozzarella bào", "Phô mai Parmesan"),
            listOf("Làm sốt bò bằm và sốt béchamel: nấu bơ với bột mì, đổ sữa từ từ khuấy đến sệt.", "Phết một lớp sốt bò vào khay nướng, xếp lá lasagne, phết béchamel, rắc phô mai.", "Lặp lại 3 lớp, lớp trên cùng phủ béchamel và nhiều Mozzarella.", "Đậy giấy bạc, nướng 180 độ C trong 30 phút, bỏ giấy bạc nướng thêm 15 phút đến vàng.", "Để nguội 10 phút rồi cắt miếng.")),
        d("a04", "Pizza Margherita", AU, "Ý", "chay", "Pizza Margherita",
            listOf("300g bột mì, 1 muỗng cà phê men, muối", "200ml nước ấm", "150g sốt cà chua", "150g phô mai Mozzarella", "Lá húng quế tươi", "Dầu ô liu"),
            listOf("Trộn bột mì, men, muối, nước ấm, nhào 10 phút, ủ 1 giờ cho nở gấp đôi.", "Cán bột thành hình tròn mỏng, phết sốt cà chua.", "Xé Mozzarella rải đều.", "Nướng 230 độ C trong 10 đến 12 phút đến khi viền bánh vàng, phô mai chảy.", "Rắc lá húng quế, rưới dầu ô liu.")),
        d("a05", "Bít tết bò sốt tiêu", AU, "Pháp", "thit", "Steak",
            listOf("2 miếng thăn bò (200g mỗi miếng)", "2 muỗng canh bơ", "2 tép tỏi, nhánh húng tây", "Muối, tiêu đen", "Khoai tây nghiền hoặc rau củ ăn kèm", "100ml kem tươi, 1 muỗng canh tiêu xanh"),
            listOf("Để thịt ra ngoài tủ lạnh 30 phút, thấm khô, ướp muối và tiêu.", "Làm nóng chảo gang rất nóng, áp chảo mỗi mặt 2 đến 3 phút (chín vừa).", "Thêm bơ, tỏi, húng tây, tưới bơ lên thịt 1 phút rồi để thịt nghỉ 5 phút.", "Làm sốt: cho kem tươi và tiêu xanh vào chảo vừa áp chảo, đun sệt; rưới lên thịt.")),
        d("a06", "Gà nướng bơ tỏi", AU, "Anh", "thit", "Roast chicken",
            listOf("1 con gà (1,2 đến 1,5 kg)", "60g bơ mềm", "5 tép tỏi băm", "Húng tây hoặc hương thảo", "1 quả chanh", "Khoai tây, cà rốt", "Muối, tiêu"),
            listOf("Trộn bơ với tỏi, thảo mộc, muối, tiêu.", "Thoa bơ lên da và vào trong bụng gà, nhét nửa quả chanh vào bụng.", "Xếp khoai tây, cà rốt vào khay, đặt gà lên trên.", "Nướng 200 độ C khoảng 60 đến 75 phút, thỉnh thoảng múc nước tiết rưới lên.", "Chọc đùi gà thấy nước trong là chín; để nghỉ 10 phút rồi cắt.")),
        d("a07", "Ratatouille rau củ nướng", AU, "Pháp", "chay", "Ratatouille",
            listOf("1 quả cà tím", "2 quả bí ngòi", "2 quả ớt chuông", "3 quả cà chua", "1 củ hành tây, 3 tép tỏi", "Dầu ô liu, húng tây, muối, tiêu"),
            listOf("Cắt tất cả rau củ thành miếng vuông cỡ 2cm.", "Phi hành tỏi với dầu ô liu, cho cà tím xào mềm.", "Lần lượt thêm ớt chuông, bí ngòi, cà chua cùng húng tây.", "Đậy nắp, hầm lửa nhỏ 30 đến 40 phút đến khi rau mềm, sánh.", "Nêm nếm, ăn nóng hoặc nguội với bánh mì.")),
        d("a08", "Súp hành tây kiểu Pháp", AU, "Pháp", "thit", "French onion soup",
            listOf("4 củ hành tây lớn thái mỏng", "40g bơ", "1 lít nước dùng bò hoặc gà", "Bánh mì baguette cắt lát", "100g phô mai Mozzarella bào", "Muối, tiêu, húng tây"),
            listOf("Xào hành tây với bơ lửa nhỏ 30 đến 40 phút, khuấy đều đến khi vàng nâu.", "Đổ nước dùng và húng tây, đun nhỏ lửa 20 phút; nêm muối, tiêu.", "Múc súp ra tô chịu nhiệt, đặt lát bánh mì và phủ phô mai.", "Nướng trong lò đến khi phô mai chảy vàng.")),
        d("a09", "Bánh Tiramisu", AU, "Ý", "trang_mieng", "Tiramisu",
            listOf("250g phô mai Mascarpone", "3 lòng đỏ trứng, 60g đường", "150ml kem tươi", "200ml cà phê espresso nguội", "Bánh Savoiardi (ladyfinger)", "Bột cacao"),
            listOf("Đánh lòng đỏ với đường đến khi bông mịn, trộn đều với Mascarpone.", "Đánh kem tươi bông cứng rồi nhẹ tay trộn vào hỗn hợp Mascarpone.", "Nhúng nhanh bánh Savoiardi vào cà phê, xếp một lớp vào khay, phủ một lớp kem; lặp lại thêm một lớp nữa.", "Để tủ lạnh ít nhất 6 giờ, rây bột cacao lên trước khi dùng.")),
        d("a10", "Panna cotta dâu tây", AU, "Ý", "trang_mieng", "Panna cotta",
            listOf("300ml kem tươi", "150ml sữa tươi", "50g đường", "1 muỗng cà phê vani", "6g gelatin", "Sốt dâu tây hoặc mứt dâu"),
            listOf("Ngâm gelatin trong nước lạnh 5 phút cho mềm.", "Đun kem tươi, sữa, đường lửa nhỏ đến khi đường tan (không để sôi), thêm vani.", "Tắt bếp, cho gelatin đã vắt ráo vào khuấy tan.", "Rót vào ly, để nguội rồi cho vào tủ lạnh 4 đến 6 giờ.", "Rưới sốt dâu lên trên khi ăn.")),

        // ============================ MÓN NHẬT ============================
        d("n01", "Sushi cuộn", NHAT, "Nhật Bản", "hai_san", "Sushi",
            listOf("2 chén cơm gạo Nhật", "3 muỗng canh giấm gạo, 1 muỗng canh đường, nửa muỗng cà phê muối", "4 tờ rong biển nori", "Cá hồi tươi hoặc tôm luộc", "Dưa leo, bơ", "Wasabi, nước tương"),
            listOf("Trộn giấm, đường, muối vào cơm còn ấm rồi để nguội.", "Trải rong biển lên mành tre, dàn cơm mỏng, chừa mép trên 1cm.", "Xếp cá hồi, dưa leo, bơ thành hàng ở giữa.", "Cuộn chặt tay bằng mành tre, dùng dao ướt cắt thành 8 khoanh.", "Dùng với wasabi và nước tương.")),
        d("n02", "Mì Ramen thịt heo", NHAT, "Nhật Bản", "thit", "Ramen",
            listOf("2 vắt mì ramen", "600ml nước dùng gà hoặc xương", "2 muỗng canh nước tương, 1 muỗng canh miso", "200g thịt ba chỉ cuộn", "2 quả trứng luộc lòng đào", "Hành lá, nấm, rong biển", "Dầu mè"),
            listOf("Cuộn thịt ba chỉ, buộc chặt, om với nước tương, đường, nước 40 đến 50 phút cho mềm rồi thái lát.", "Luộc trứng 6,5 phút, ngâm vào nước om thịt.", "Đun nước dùng với nước tương, miso và dầu mè.", "Luộc mì theo hướng dẫn, vớt ráo cho vào tô.", "Chan nước dùng, xếp thịt, nửa quả trứng, hành lá, rong biển.")),
        d("n03", "Tempura tôm", NHAT, "Nhật Bản", "hai_san", "Tempura",
            listOf("8 con tôm lớn", "100g bột mì, 1 quả trứng, 150ml nước đá lạnh", "Bí đỏ, cà rốt, khoai lang thái lát", "Dầu ăn để chiên", "100ml nước dashi, 2 muỗng canh nước tương, 1 muỗng canh mirin", "Củ cải trắng bào"),
            listOf("Bóc vỏ tôm chừa đuôi, rạch vài đường ở bụng để tôm thẳng.", "Trộn bột mì, trứng, nước đá lạnh sơ sơ (để vón cục nhỏ, không khuấy mịn).", "Nhúng tôm và rau củ vào bột, chiên dầu 170 độ C đến khi vàng nhạt, giòn rồi vớt ra giấy thấm dầu.", "Đun sôi dashi, nước tương, mirin làm nước chấm; ăn kèm củ cải bào.")),
        d("n04", "Mì Udon nước dùng", NHAT, "Nhật Bản", "thit", "Udon",
            listOf("2 vắt mì udon", "600ml nước dashi", "2 muỗng canh nước tương nhạt, 1 muỗng canh mirin", "100g thịt gà hoặc thịt bò thái mỏng", "Hành lá, nấm, rau cải"),
            listOf("Đun sôi nước dashi với nước tương và mirin.", "Cho thịt vào nấu chín, thêm nấm và rau cải.", "Luộc mì udon theo hướng dẫn, vớt ráo cho vào tô.", "Chan nước dùng và rau thịt lên mì, rắc hành lá.")),
        d("n05", "Okonomiyaki", NHAT, "Nhật Bản", "thit", "Okonomiyaki",
            listOf("150g bột mì", "150ml nước dashi hoặc nước", "2 quả trứng", "300g bắp cải thái nhỏ", "100g thịt ba chỉ thái mỏng", "Sốt okonomi, mayonnaise, bột rong biển, hành lá"),
            listOf("Trộn bột mì, nước dashi, trứng thành hỗn hợp sệt.", "Cho bắp cải, hành lá vào trộn đều.", "Đổ hỗn hợp ra chảo nóng thành bánh dày, xếp thịt ba chỉ lên trên.", "Chiên mỗi mặt 4 đến 5 phút đến vàng, chín thịt.", "Phết sốt, rưới mayonnaise, rắc rong biển.")),
        d("n06", "Tonkatsu heo chiên xù", NHAT, "Nhật Bản", "thit", "Tonkatsu",
            listOf("2 miếng thịt heo thăn dày 2cm", "Bột mì, 1 quả trứng đánh tan, bột chiên xù panko", "Dầu chiên", "Bắp cải thái chỉ", "Sốt tonkatsu", "Muối, tiêu"),
            listOf("Rạch gân thịt, ướp muối tiêu.", "Lăn thịt qua bột mì, trứng, rồi bột panko và ấn chặt.", "Chiên ngập dầu 170 độ C khoảng 5 đến 6 phút, vàng giòn hai mặt.", "Để ráo dầu, thái miếng.", "Dọn với bắp cải thái chỉ, cơm và sốt tonkatsu.")),
        d("n07", "Cơm Katsudon", NHAT, "Nhật Bản", "thit", "Katsudon",
            listOf("2 miếng tonkatsu (cách làm ở món Tonkatsu)", "2 chén cơm trắng", "1 củ hành tây thái mỏng", "3 quả trứng", "150ml nước dashi", "2 muỗng canh nước tương, 1 muỗng canh mirin, 1 muỗng canh đường"),
            listOf("Chiên tonkatsu giòn rồi thái lát.", "Đun dashi, nước tương, mirin, đường, cho hành tây vào nấu mềm.", "Xếp thịt chiên lên trên, rưới trứng đánh tan, đậy nắp nấu lửa nhỏ đến khi trứng vừa chín tới.", "Múc cơm ra tô, đặt phần thịt trứng lên trên cùng nước sốt.")),
        d("n08", "Cơm bò Gyudon", NHAT, "Nhật Bản", "thit", "Gyūdon",
            listOf("300g thịt bò ba rọi thái mỏng", "1 củ hành tây", "200ml nước dashi", "3 muỗng canh nước tương, 2 muỗng canh mirin, 1 muỗng canh đường", "2 chén cơm", "Trứng lòng đào (tùy chọn)"),
            listOf("Đun dashi với nước tương, mirin, đường đến khi sôi.", "Cho hành tây vào nấu 5 phút.", "Thêm thịt bò, nấu 3 đến 4 phút đến chín, vớt bọt.", "Múc cơm ra tô, xếp thịt hành lên, rưới nước sốt; ăn kèm trứng lòng đào.")),
        d("n09", "Cơm nắm Onigiri nhân mơ muối", NHAT, "Nhật Bản", "chay", "Onigiri",
            listOf("2 chén cơm gạo Nhật còn ấm", "4 quả mơ muối (umeboshi)", "Muối", "Rong biển nori cắt dải"),
            listOf("Thấm tay vào nước muối để cơm không dính.", "Lấy một nắm cơm, ấn lõm giữa, cho nhân mơ muối vào rồi vo kín.", "Nắm cơm thành hình tam giác, ép nhẹ tay.", "Quấn dải rong biển quanh một cạnh và dùng ngay.")),
        d("n10", "Bánh Mochi nhân đậu đỏ", NHAT, "Nhật Bản", "trang_mieng", "Mochi",
            listOf("100g bột gạo nếp", "30g đường", "150ml nước", "Bột bắp hoặc bột nếp để áo", "Nhân đậu đỏ ngọt"),
            listOf("Trộn bột nếp, đường và nước đến khi mịn.", "Đậy màng bọc, cho vào lò vi sóng 2 phút, trộn rồi thêm 1 phút nữa (hoặc hấp 20 phút).", "Đổ bột ra mặt phẳng đã rắc bột bắp, để nguội bớt.", "Chia nhỏ, ép dẹt, cho nhân đậu đỏ vào giữa và vo tròn kín.", "Ăn trong ngày để mochi mềm dẻo.")),

        // ============================ MÓN TRUNG ============================
        d("t01", "Đậu hũ Mapo Tứ Xuyên", TRUNG, "Tứ Xuyên", "thit", "Mapo doufu",
            listOf("400g đậu hũ non", "150g thịt heo xay", "2 muỗng canh tương đậu cay", "1 muỗng cà phê bột ớt, tiêu Tứ Xuyên", "3 tép tỏi, gừng băm", "Nước dùng, nước tương, bột bắp", "Hành lá"),
            listOf("Cắt đậu hũ khối vuông, chần nước muối sôi 2 phút.", "Phi tỏi gừng, xào thịt xay chín, thêm tương đậu cay xào thơm.", "Đổ nước dùng, thêm đậu hũ, đun nhỏ lửa 5 phút.", "Hòa bột bắp với nước, đổ vào cho sệt.", "Rắc tiêu Tứ Xuyên và hành lá.")),
        d("t02", "Gà xào Kung Pao", TRUNG, "Tứ Xuyên", "thit", "Kung Pao chicken",
            listOf("400g ức gà thái hạt lựu", "50g đậu phộng rang", "6 quả ớt khô", "3 tép tỏi, gừng", "2 muỗng canh nước tương, 1 muỗng canh giấm, 1 muỗng canh đường, 1 muỗng cà phê bột bắp", "Hành lá"),
            listOf("Ướp gà với nước tương, bột bắp 15 phút.", "Pha sốt từ nước tương, giấm, đường, bột bắp và chút nước.", "Xào ớt khô với dầu cho thơm, thêm tỏi, gừng.", "Cho gà vào xào chín, đổ sốt vào xào sệt.", "Cho đậu phộng và hành lá, đảo nhanh rồi tắt bếp.")),
        d("t03", "Thịt heo sốt chua ngọt", TRUNG, "Quảng Đông", "thit", "Sweet and sour pork",
            listOf("400g thịt heo thăn", "1 quả dứa, 1 quả ớt chuông", "Bột bắp, 1 quả trứng, dầu chiên", "3 muỗng canh tương cà, 2 muỗng canh giấm, 2 muỗng canh đường, 1 muỗng canh nước tương"),
            listOf("Thái thịt miếng vừa ăn, ướp muối, tẩm trứng và bột bắp.", "Chiên giòn hai lần để thịt vàng và giòn lâu.", "Đun sốt cà chua, giấm, đường, nước tương đến khi sệt.", "Xào dứa và ớt chuông sơ, cho thịt vào đảo nhanh với sốt; dùng nóng với cơm.")),
        d("t04", "Cơm chiên trứng", TRUNG, "Dương Châu", "chay", "Egg fried rice",
            listOf("2 chén cơm nguội", "3 quả trứng", "2 củ hành lá", "Cà rốt, đậu Hà Lan", "1 muỗng canh nước tương", "Dầu, muối, tiêu"),
            listOf("Dùng cơm nguội để hạt rời.", "Chiên trứng khuấy tơi trên chảo nóng rồi múc ra.", "Xào cà rốt, đậu Hà Lan, cho cơm vào đảo đều tay.", "Thêm trứng, nước tương, muối, tiêu rồi rắc hành lá.")),
        d("t05", "Mì xào Chow mein", TRUNG, "Quảng Đông", "thit", "Chow mein",
            listOf("200g mì trứng", "150g thịt gà hoặc thịt heo thái mỏng", "Cải thìa, cà rốt, giá đỗ", "2 tép tỏi", "2 muỗng canh nước tương, 1 muỗng canh dầu hào", "Dầu mè"),
            listOf("Luộc mì gần chín, vớt ra trộn ít dầu.", "Xào thịt với tỏi đến khi chín, múc ra.", "Xào rau củ trên lửa lớn, thêm mì và thịt.", "Nêm nước tương, dầu hào, dầu mè và đảo đều đến khi mì ráo, hơi cháy cạnh.")),
        d("t06", "Sủi cảo", TRUNG, "Bắc Kinh", "thit", "Jiaozi",
            listOf("300g bột mì, 150ml nước ấm (hoặc vỏ bán sẵn)", "250g thịt heo xay", "100g tôm băm", "Hẹ hoặc cải thảo băm", "Gừng, nước tương, dầu mè", "Nước chấm: nước tương, giấm, ớt"),
            listOf("Nhào bột với nước thành khối mịn, ủ 30 phút rồi cán mỏng cắt hình tròn.", "Trộn thịt, tôm, hẹ, gừng, nước tương, dầu mè làm nhân.", "Cho nhân vào giữa vỏ, gấp mép và ép chặt.", "Luộc trong nước sôi đến khi nổi lên, hoặc áp chảo rồi hấp nước; dùng với nước chấm.")),
        d("t07", "Hoành thánh", TRUNG, "Quảng Đông", "thit", "Wonton",
            listOf("200g thịt heo xay", "100g tôm băm", "Vỏ hoành thánh", "Hành lá, tỏi, tiêu", "500ml nước dùng xương", "Cải thìa", "Nước tương, dầu mè"),
            listOf("Trộn thịt, tôm với hành, tiêu, nước tương.", "Đặt nhân lên vỏ, gấp lại thành hình nhúm.", "Đun nước dùng với chút nước tương, nêm vừa ăn.", "Luộc hoành thánh đến khi nổi, thêm cải thìa; múc ra tô và rưới dầu mè.")),
        d("t08", "Súp chua cay", TRUNG, "Tứ Xuyên", "thit", "Hot and sour soup",
            listOf("100g thịt heo thái sợi", "100g đậu hũ thái sợi", "Nấm đông cô, mộc nhĩ", "1 quả trứng đánh tan", "700ml nước dùng", "2 muỗng canh giấm, 1 muỗng cà phê tiêu", "Nước tương, bột bắp, hành lá"),
            listOf("Đun sôi nước dùng cùng nấm, mộc nhĩ.", "Cho thịt và đậu hũ vào nấu 5 phút.", "Nêm nước tương, giấm, tiêu cho vị chua cay.", "Hòa bột bắp với nước, đổ vào cho sệt.", "Rót trứng thành sợi mảnh, rắc hành lá.")),
        d("t09", "Thịt xá xíu", TRUNG, "Quảng Đông", "thit", "Char siu",
            listOf("500g thịt heo thăn hoặc ba chỉ", "2 muỗng canh mật ong", "2 muỗng canh nước tương", "1 muỗng canh dầu hào", "1 muỗng canh ngũ vị hương", "Tỏi băm, đường"),
            listOf("Trộn gia vị làm nước ướp, ướp thịt ít nhất 4 giờ hoặc để qua đêm.", "Xếp thịt vào khay có giá nướng.", "Nướng 200 độ C khoảng 25 đến 30 phút, quét thêm nước ướp và mật ong, lật thịt giữa chừng.", "Để nghỉ 10 phút rồi thái lát.")),
        d("t10", "Chè trôi nước kiểu Trung", TRUNG, "Giang Nam", "trang_mieng", "Tangyuan",
            listOf("150g bột nếp", "Nhân mè đen hoặc đậu phộng giã", "1 lít nước, 100g đường, vài lát gừng"),
            listOf("Nhào bột nếp với nước ấm thành khối mềm mịn.", "Chia bột nhỏ, ấn lõm, cho nhân vào rồi vo tròn.", "Nấu nước đường với gừng.", "Thả viên bột vào nước sôi, chờ nổi lên thêm 2 phút.", "Vớt ra bát, chan nước đường gừng."))
    )

    val all: List<MealDetail> = dishes.map { it.meal }
    val wikiTitles: Map<String, String> = dishes.associate { it.meal.id to it.wiki }
}
