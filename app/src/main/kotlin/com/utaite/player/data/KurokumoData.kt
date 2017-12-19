package com.utaite.player.data

import com.utaite.player.R
import io.realm.Realm


class KurokumoData {

    companion object {

        fun init() {
            val realm = Realm.getDefaultInstance()
            val dataSet: List<Data> = listOf(
                    newData(R.string.title_interviewer, "17999838"),
                    newData(R.string.title_kimi_no_shiranai_monogatari, "18841018"),
                    newData(R.string.title_ama_no_zaku, "19934014"),
                    newData(R.string.title_lost_one_no_goukoku, "20289162"),
                    newData(R.string.title_lost_time_memory, "21429361"),
                    newData(R.string.title_seisou_bakuretsu_boy, "21778350"),
                    newData(R.string.title_koshitantan, "22226734"),
                    newData(R.string.title_okochama_sensou, "22433672"),
                    newData(R.string.title_donut_hole, "23257026"),
                    newData(R.string.title_sekai_wa_koi_ni_ochiteiru, "24176925"),
                    newData(R.string.title_unravel, "24277186"),
                    newData(R.string.title_higai_mousou_keitai_joshi_wara, "24288214"),
                    newData(R.string.title_gishinanki, "24358206"),
                    newData(R.string.title_ringo_uri_no_utakata_shoujo, "24464111"),
                    newData(R.string.title_hibikase, "24586945"),
                    newData(R.string.title_regret, "24644837"),
                    newData(R.string.title_amekigoe_zankyou, "24841778"),
                    newData(R.string.title_ima_suki_ni_naru, "25024924"),
                    newData(R.string.title_for_you, "25066596"),
                    newData(R.string.title_oni_kyokan, "25147792"),
                    newData(R.string.title_attakain_dakara, "25349688"),
                    newData(R.string.title_yomosugara_kimi_omofu, "25418784"),
                    newData(R.string.title_ai_no_scenario, "25438939"),
                    newData(R.string.title_amaoto_noise, "25535474"),
                    newData(R.string.title_yoake_to_hotaru, "25656348"),
                    newData(R.string.title_leave, "25879853"),
                    newData(R.string.title_asagao_no_chiru_koro_ni, "26085843"),
                    newData(R.string.title_mairieux, "26265326"),
                    newData(R.string.title_tsuyuake_no, "26477554"),
                    newData(R.string.title_dokusenyoku, "26599776"),
                    newData(R.string.title_ashita_sekai_ga_horobunara, "26669563"),
                    newData(R.string.title_end_tree, "26744607"),
                    newData(R.string.title_pride_kakumei, "26904732"),
                    newData(R.string.title_daze, "26933575"),
                    newData(R.string.title_ao, "27077345"),
                    newData(R.string.title_luvoratorrrrry, "27219907"),
                    newData(R.string.title_mousou_shikkan_girl, "27325250"),
                    newData(R.string.title_usotsuki, "27421061"),
                    newData(R.string.title_watashi_no_r, "27631776"),
                    newData(R.string.title_a_little_pain, "28077040"),
                    newData(R.string.title_tatoeba_ima_koko_ni_okareta_hana_ni, "28155601"),
                    newData(R.string.title_yuudachi_no_ribbon, "28395018"),
                    newData(R.string.title_kokoro_toka_iu_namae_no_mihakken_no_zouki_no_kinou_ni_tsuite_no_kousatsu, "28478997"),
                    newData(R.string.title_amaoto_petrichor, "28761490"),
                    newData(R.string.title_marunouchi_sadistic, "28877702"),
                    newData(R.string.title_moshimo_hitori_nokosarete_sekai_ga_uso_ja_nai_nara, "29049325"),
                    newData(R.string.title_rapunzel, "29280551"),
                    newData(R.string.title_donor_song, "29568654"),
                    newData(R.string.title_sore_ga_anata_no_shiawase_toshitemo, "29620980"),
                    newData(R.string.title_690000000, "29912142"),
                    newData(R.string.title_aishite_aishite_aishite, "30031815"),
                    newData(R.string.title_mousou_kanshou_daishou_renmei, "30141949"),
                    newData(R.string.title_shikiori_no_hane, "30332635"),
                    newData(R.string.title_charles, "30587156"),
                    newData(R.string.title_alienate, "30791607"),
                    newData(R.string.title_kawaiku_naritai, "31052027"),
                    newData(R.string.title_ifuudoudou, "31170928"),
                    newData(R.string.title_hikou_shoujo, "31414093"),
                    newData(R.string.title_redire, "31527661"),
                    newData(R.string.title_hibana, "31780650"),
                    newData(R.string.title_nounai_kakumei_girl, "31934756"),
                    newData(R.string.title_yomei_mikka_shoujo, "32086021"),
                    newData(R.string.title_mitsugetsu_un_deux_trois, "32234324"),
                    newData(R.string.title_macaron, "32387298")
                    )
            realm.setDataSet(dataSet)
        }

    }

}

private fun newData(title: Int, url: String): Data =
        Data(utaite = R.string.utaite_kurokumo, title = title, url = url)
