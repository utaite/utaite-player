package com.utaite.player.data

import com.utaite.player.R
import io.realm.Realm


class HiinaData {

    companion object {

        fun init() {
            val realm = Realm.getDefaultInstance()
            val dataSet: List<Data> = listOf(
                    newInstance(R.string.title_mousou_zei, "22328675"),
                    newInstance(R.string.title_aimai_elegy, "22509592"),
                    newInstance(R.string.title_dokusenyoku, "22858314"),
                    newInstance(R.string.title_suki_suki_zecchou_shou, "22897415"),
                    newInstance(R.string.title_heart_realize, "23222501"),
                    newInstance(R.string.title_daze, "23329521"),
                    newInstance(R.string.title_milk_crown_on_sonnetica, "23499430"),
                    newInstance(R.string.title_renai_saiban, "23769716"),
                    newInstance(R.string.title_mememememe, "23882481"),
                    newInstance(R.string.title_tsuki_ni_tawagoto, "23970509"),
                    newInstance(R.string.title_tsugihagi_staccato, "24136099"),
                    newInstance(R.string.title_sekai_wa_koi_ni_ochiteiru, "24213668"),
                    newInstance(R.string.title_asu_no_yozora_shoukaihan, "24313819"),
                    newInstance(R.string.title_music_music, "24417109"),
                    newInstance(R.string.title_ura_omote_fortune, "24614723"),
                    newInstance(R.string.title_mikazuki_no_yakusoku, "24655607"),
                    newInstance(R.string.title_hibikase, "24788445"),
                    newInstance(R.string.title_hide_and_seek, "24863592"),
                    newInstance(R.string.title_yoake_to_hotaru, "24898847"),
                    newInstance(R.string.title_mellow_birthday_eve, "25338454"),
                    newInstance(R.string.title_chopin_to_koori_no_hakken, "25389533"),
                    newInstance(R.string.title_hitoribocchi_to_kokoro_no_hon_to, "25451107"),
                    newInstance(R.string.title_ai_no_scenario, "25495867"),
                    newInstance(R.string.title_sayonara_dake_ga_jinsei_da, "25592051"),
                    newInstance(R.string.title_fubuki, "25659052"),
                    newInstance(R.string.title_kuusou_ressha, "25750462"),
                    newInstance(R.string.title_puzzle_girl, "25886142"),
                    newInstance(R.string.title_asagao_no_chiru_koro_ni, "25951420"),
                    newInstance(R.string.title_onegai_darling, "26160713"),
                    newInstance(R.string.title_sing_like_a_magic, "26253876"),
                    newInstance(R.string.title_akatsuki_zukuyo, "26405212"),
                    newInstance(R.string.title_ameiro_kokoro_moyou, "26611297"),
                    newInstance(R.string.title_aira, "26677457"),
                    newInstance(R.string.title_futariboshi, "26923454"),
                    newInstance(R.string.title_kimi_no_yozora_shoukaihan, "27007009"),
                    newInstance(R.string.title_shihatsu_to_kafka, "27099786"),
                    newInstance(R.string.title_natsu_ni_sarishi_kimi_wo_omou, "27139725"),
                    newInstance(R.string.title_aishite, "27659809"),
                    newInstance(R.string.title_sayonara_hana_dorobou_san, "27694295"),
                    newInstance(R.string.title_suji, "27819211"),
                    newInstance(R.string.title_yamiyo_no_dance, "27891443"),
                    newInstance(R.string.title_sayonara_souvenir, "27898703"),
                    newInstance(R.string.title_gingaroku, "27973043"),
                    newInstance(R.string.title_shiro_yuki, "28422184"),
                    newInstance(R.string.title_koiiro_ni_sake, "28539719"),
                    newInstance(R.string.title_negaigoto, "28721885"),
                    newInstance(R.string.title_pu_pe_pi_pe, "28912934"),
                    newInstance(R.string.title_kumamiko_dancing, "28964348"),
                    newInstance(R.string.title_tsumi_no_namae, "29054975"),
                    newInstance(R.string.title_rapunzel, "29200132"),
                    newInstance(R.string.title_hate_ni_wa_hatena, "29367430"),
                    newInstance(R.string.title_synchronizer, "29433144"),
                    newInstance(R.string.title_nanairo_no_asa, "29790052"),
                    newInstance(R.string.title_nandemonaiya, "29894591"),
                    newInstance(R.string.title_pumpkin_no_kokkei_na_yume, "29945736"),
                    newInstance(R.string.title_heart_no_atoaji, "30318888"),
                    newInstance(R.string.title_mawaru_sora_usagi, "30413518"),
                    newInstance(R.string.title_kawaiku_naritai, "30469609"),
                    newInstance(R.string.title_aozora_no_rhapsody, "30581318"),
                    newInstance(R.string.title_moment, "30662327"),
                    newInstance(R.string.title_atafuta_ashita, "30678423"),
                    newInstance(R.string.title_kagerou_variation, "30823936"),
                    newInstance(R.string.title_lets_nyance, "30841007"),
                    newInstance(R.string.title_toumei_shoujo_no_susume, "31099788"),
                    newInstance(R.string.title_near, "31512604"),
                    newInstance(R.string.title_teo, "31788262"),
                    newInstance(R.string.title_shinu_ni_wa_Ii_hi_datta, "32092419"),
                    newInstance(R.string.title_re_re_realize, "32114284"),
                    newInstance(R.string.title_positive_dance_time, "32136298")
            )
            realm.setDataSet(dataSet)
        }

    }

}

private fun newInstance(title: Int, url: String): Data =
        Data(utaite = R.string.utaite_hiina, title = title, url = url)
