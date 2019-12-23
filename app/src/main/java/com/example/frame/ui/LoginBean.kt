package com.example.frame.ui

data class LoginBean(
        val isDefaultPwdModified: Int,
        val menu: Menu,
        val realName: String,
        val sysUserId: Int,
        val token: String,
        val userId: String,
        val avatar: String,
        /**
         * 用户类型

        1-直营代理商(BDM)

        2-直营授理商(BD)

        3-非直营代理商

        4-非直营授理商

        5-非直营超级授理商

        6-非直营市场经理
         */
        val userType: Int,
        /**
         * 数字经营助手权限（1启用；2未启用
         */
        val openFlag: Int,
        /**
         * 剩余数字经营助手套数
         */
        val remainingCount: Int
) {
    data class Menu(
            val dataCenter: Int,
            val bdMyScore: Int,
            val bdList: Int,
            val bdmMyScore: Int,
            val bdmTeamList: Int,
            val bdmBdList: Int
    )
}

