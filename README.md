# ClansLite
ClansLite is a light-weight clans plugin for Minecraft servers running Spigot or any of its forks!

ClansLite does not support any grief prevention tools such as land claiming or securing containers within your clan.

ClansLite DOES however offer the ability to disable friendly fire within your clan!

## /clan command
Aliases: `/clans`, `/c`  
  
The `/clan` command is the main command of the plugin, with `/clan` you can do the following:
* `/clan create <name>` - Creates A new clan if not already in one
* `/clan disband` - If you are the clan owner, this will destroy your clan
* `/clan invite <player>` - Will invite a player to your clan if they are not already in one
* `/clan kick <player>` - Will kick a player from your clan
* `/clan info` - Will display information about your current clan
* `/clan list` - Will list all clans in the server
* `/clan prefix <prefix>` - Will change the prefix for your clan in chat
* `/clan ally [add|remove] <ally-owner>` - Will either add or remove an allied clan to yours
* `/clan pvp` - Will toggle the friendly fire status for your clan
* `/clan [sethome|home]` - Will set a clan home location or teleport you or you clan members to this location.

## /clanadmin command
Aliases: `/ca`  

The `/clanadmin` command is purely for server admins only. 

2 arguments are implemented which are: 
* `/clanadmin save` - which will save all current clan info to the `clans.yml` data file.  
* `/clanadmin reload` - This reloads the plugins `config.yml` & the `messages.yml` files from disk.

## Permissions
ClansLite comes with 4 permissions:
* `clanslite.*`
* `clanslite.clan`
* `clanslite.admin`
* `clanslite.update`

`clanslite.*` is a permission to allow access to ALL functions regardless of operator status.
`clanslite.clan` is by default given to everyone so they can all create, edit and manage a clan.  
`clanslite.admin` is by default given to server operators.
`clanslite.update` is the permission node to allow a player to see in game notifications if there is a plugin update available.

## Config
The max clan size (by default is 8), can be managed in the `plugins/ClansLite/config.yml` file.

The max clan allies (by default is 4), can be managed in the `plugins/ClansLite/config.yml` file.

## Chat prefix
ClansLite exposes a variable of `{CLAN}` to use in Essentials Chat or similar.

## PlaceholderAPI
ClansLite exposes `4` external placeholders using `PlaceholderAPI` to enable the fetching of a players clan name or the clan prefix.
The two available placeholders are:
* `%clansLite_clanName%`
* `%clansLite_clanPrefix%`
* `%clansLite_friendlyFire%`
* `%clansLite_clanHomeSet%`

To be able to use these The latest release of [PlaceholderAPI](https://www.spigotmc.org/resources/placeholderapi.6245/) MUST be installed!  Without it, only the above `{CLAN}` will be available.

###Please report any issues in GitHub and feel free to join my [discord](https://discord.gg/ZECTYBw5qr).

###Thank you for using my plugin!
