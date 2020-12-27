package at.markusnentwich.helicon.security

const val ROOT_ROLE = "root"
const val OWNER_ROLE = "owner"
const val MONITOR_ROLE = "monitor"

const val ASSET_ROLE = "asset"
const val ACCOUNT_ROLE = "account"
const val CATALOGUE_ROLE = "catalogue"
const val ORDER_ROLE = "order"
const val META_ROLE = "meta"

val ALL_ROLES = listOf(ASSET_ROLE, ACCOUNT_ROLE, CATALOGUE_ROLE, ORDER_ROLE, META_ROLE)
