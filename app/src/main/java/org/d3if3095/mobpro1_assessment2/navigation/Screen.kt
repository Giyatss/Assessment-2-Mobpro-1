package org.d3if3095.mobpro1_assessment2.navigation

import org.d3if3095.mobpro1_assessment2.ui.screen.KEY_ID_MOBIL

sealed class Screen(val route: String) {
    data object Home: Screen("mainScreen")
    data object FormBaru: Screen("detailScreen")
    data object FormUbah: Screen("detailScreen/{$KEY_ID_MOBIL}") {
        fun withId(id: Long) = "detailScreen/$id"
    }
}