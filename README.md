[![](https://jitpack.io/v/Loving11ish/ClansLite.svg)](https://jitpack.io/#Loving11ish/ClansLite)

~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
### ClansLite
ClansLite is a light-weight clans plugin for Minecraft servers running Spigot or any of its forks!

ClansLite does not support any grief prevention tools such as land claiming, it does support customisable single chest protections.

Want to try out the very latest dev builds?
https://github.com/Loving11ish/ClansLite-Beta-Builds/releases
Use the above dev build at your own risk! Please follow any and ALL instructions provided with EACH dev build released!

~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

* /clan command
Aliases: /clans, /c, /cl

The /clan command is the main command of the plugin, with /clan you can do the following:

/clan create <name> - Creates A new clan if not already in one
/clan disband - If you are the clan owner, this will destroy your clan
/clan leave - If you are in a clan, this will remove you from it
/clan invite <player> - Will invite a player to your clan if they are not already in one
/clan join - Will add you to a clan that you have been invited too.
/clan kick <player> - Will kick a player from your clan
/clan info - Will display information about your current clan
/clan list - Will list all clans in the server
/clan prefix <prefix> - Will change the prefix for your clan in chat
/clan ally [add|remove] <ally-owner> - Will either add or remove an allied clan to yours.
/clan enemy [add|remove] <ally-owner> - Will either add or remove an enemy clan to yours.
/clan pvp - Will toggle the friendly fire status for your clan
/clan [sethome|delhome|home] - Will set a clan home location or teleport you or your clan members to this location.
/clan transfer <player> - Transfer your clan to another owner.
/clan points [withdraw|deposit] <amount> - Deposit or withdraw points from/to your clan.

~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

* /cc command*
Aliases: /clanchat, /clanc, /cchat, /chat

The /cc command is for the sole purpose of utilising the per clan chat.  The following syntax is accepted:

/cc <message> - This will send a message to only the members of YOUR clan or the clan you are in.

~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

* /chest command*
Aliases: /cchest, /cch, /cht

The /chest command is the base command to use the chest protection system, with /chest you can do the following:
/chest lock - Lock the single chest that you're looking at.
/chest unlock - Unlock the single chest that you're looking at.
/chest accesslist - See what players have access to that chest.
/chest buylock <amount> - Purchase a new chest lock or multiple.

~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

* /playerpoints command*
Aliases: /pp, /points, /ppoints

The /playerpoints command is the command to see your or others points, with /playerpoints you can do the following:
/playerpoints [listall] - See your or other players points value.

~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

* /chatspy command*
Aliases: /cs, /ccs, /clanchatspy, /spy

The /chatspy command allows admins to spy on the secret clan chats, with /chatspy you can do the following:
/chatspy - Toggle the clan chat spying ability.

~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

* /clanadmin command*
Aliases: /ca, /cla

The /clanadmin command is purely for server admins only. 4 arguments are implemented which are:
/clanadmin save - which will save all current clan info to the clans.yml data file.
/clanadmin reload - This reloads the plugins config.yml & the messages.yml files from disk.
/clanadmin disband <owner-name> - This allows admins to delete any unauthorised clans.
/clanadmin about - This give you an overview of the plugin's core information.

~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

* Permissions
ClansLite comes with 19 permissions:

clanslist.*
clanslite.clan
clanslite.admin
clanslite.update
clanslite.bypass
clanslite.bypass.*
clanslite.bypass.homecooldown
clanslite.bypass.chatcooldown
clanslite.bypass.chests
clanslite.bypass.pvp
clanslite.chat.spy
clanslite.points.listall
clanslite.maxclansize.*
clanslite.maxclansize.group1
clanslite.maxclansize.group2
clanslite.maxclansize.group3
clanslite.maxclansize.group4
clanslite.maxclansize.group5
clanslite.maxclansize.group6

`clanslite.*` - is a permission to allow access to ALL functions regardless or operator status.

`clanslite.clan` - is by default given to everyone so they can all create, edit and manage a clan.

`clanslite.admin` - is by default given to server operators.

`clanslist.update` - is the permission node to allow a player to see in game notifications if there is a plugin update available.

`clanslite.bypass` - is the permission node to allow a player to bypass all protections and cooldowns.

`clanslite.bypass.*` - is the permission node to allow a player to bypass all protections and cooldowns.

`clanslite.bypass.homecooldown` - is the permission node to allow a player to bypass the home command cooldown.

`clanslite.bypass.chatcooldown` - is the permission node to allow a player to bypass the clan chat command cooldown.

`clanslite.bypass.chests` - is the permission node to allow a player to bypass the chest protection.

`clanslite.bypass.pvp` -  is the permission node to allow a player to bypass the friendly fire protections.

`clanslite.chat.spy` - is the permission node to allow a player to toggle clan chat spy.

`clanslite.points.listall` - is the permission node to view all players total player point values.

`clanslite.maxclansize.*` - is the permission node to allow adding an unlimited number of clan members.

`clanslite.maxclansize.group1` - is the permission node to allow only group 1 size of clan.

`clanslite.maxclansize.group2` - is the permission node to allow only group 2 size of clan.

`clanslite.maxclansize.group3` - is the permission node to allow only group 3 size of clan.

`clanslite.maxclansize.group4` - is the permission node to allow only group 4 size of clan.

`clanslite.maxclansize.group5` - is the permission node to allow only group 5 size of clan.

`clanslite.maxclansize.group6` - is the permission node to allow only group 6 size of clan.

~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

* Configs

[SPOILER="config.yml"]
[code]
#~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~#
#~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~#
#                                        ----[ClansLite]----                                        #
#                                     ----[By Loving11ish]----                                      #
#                                  ----[Contributors: Gamlin]----                                   #
#~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~#
#                                   ----[Plugin Config File]----                                    #
#~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~#
#~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~#
#ClansLite config for 1.3.9

#Do you want to use the GUI system? [Default value: true]
use-global-GUI-system: true

clan-tags:
  #Set the minimum length of the clan prefix and name. [Default value: 3]
  min-character-limit: 3
  #Set the maximum length of the clan prefix and name. [Default value: 32]
  max-character-limit: 32
  #Set below names that are not allowed to be used in prefixes or names. [They ARE case & syntax sensitive]
  disallowed-tags:
    - "Gamers"
    - "Rise"
    - "Up"
  #Add a space after the clan prefix in chat. [Default value: true]
  prefix-add-space-after: true
  #Add `[]` characters before and after the clan prefix in the chat. [Default value: false]
  prefix-add-brackets: false
  #Below is how the above brackets should appear.
  brackets-opening: "&f["
  brackets-closing: "&f]"

clan-size:
  #Set the default maximum amount of members that can join a players' clan. [Default value: 8]
  default-max-clan-size: 8
  #To allow the use of a tiered permission system for clan sizes instead of the global amount, use the system below.
  tiered-clan-system:
    #Enable the tiered system use. [Default value: false]
    enabled: false
    permission-group-list:
      group-1: 4  #Perm: 'clanslite.maxclansize.group1' [This perm is given by default]
      group-2: 8  #Perm: 'clanslite.maxclansize.group2'
      group-3: 12 #Perm: 'clanslite.maxclansize.group3'
      group-4: 16 #Perm: 'clanslite.maxclansize.group4'
      group-5: 20 #Perm: 'clanslite.maxclansize.group5'
      group-6: 24 #Perm: 'clanslite.maxclansize.group6'

clan-creation:
  #Do you want a message to be sent to all players when a new clan is created? [Default value: true]
  announce-to-all: true
  #Do you want the message sent as a title instead of a chat message? [Default value: true]
  send-as-title: true

clan-join:
  #Do you want a message to be sent to all players when a player joins a clan? [Default value: true]
  announce-to-all: true
  #Do you want the message sent as a title instead of a chat message? [Default value: false]
  send-as-title: false

clan-chat:
  #Enable the clan chat system. [Default value: true]
  enabled: true
  #Below is the prefix for the clan chat messages. [Default value: &6[&3CC&6]&r]
  chat-prefix: "&6[&3CC&6]&r"
  cool-down:
    #Enable the cool down on the '/cc <message>' command to prevent chat spamming (RECOMMENDED). [Default value: true]
    enabled: true
    #Cool-down time in seconds. [Default value: 5 = 5 seconds]
    time: 5
  chat-spy:
    #Do you want players with the perm 'clanslite.chat.spy' be able to spy on all clan chat messages? [Default value: true]
    enabled: true
    #Below is the prefix for th chat spy messages. [Default value: &6[&cSPY&6]&r]
    chat-spy-prefix: "&6[&cSPY&6]&r"

#Set the maximum amount of allied clans that can a clan can have. [Default value: 4]
max-clan-allies: 4

#Set the maximum amount of enemy clans that can a clan can have. [Default value: 2]
max-clan-enemies: 2

#Clan protections
protections:
  pvp:
    #Globally enable the clan friendly fire system. [Default value: true]
    pvp-command-enabled: true
    #Enable the ability for a player to bypass the pvp protection using 'clanslite.bypass.pvp'. [Default value: true]
    enable-bypass-permission: true
  chests:
    #~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~#
    #~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~#
    #THIS FEATURE IS COMING SOON!! IT IS CURRENTLY DISABLED!! PLEASE WAIT FOR THE NEXT UPDATE!!

    ##THIS FEATURE IS EXTREMELY EXPERIMENTAL CURRENTLY!##
    ##USE WITH CAUTION AND FREQUENT BACKUPS!##
    #Globaly enable the clan protected chests system. [Default value: false]
    enabled: false
    #Enable the ability for a player to bypass the chest protection using 'clanslite.bypass.chests'. [Default value: true]
    enable-bypass-permission: true
    #Allow protected chests to be broken by TNT explosions? [Default value: true]
    enable-TNT-destruction: true
    #Allow protected chests to be broken by Creeper explosions? [Default value: true]
    enable-creeper-destruction: true
    #Set the value of clan points required to purchase a new protected chest. [Default value: 250]
    clan-points-purchase-value: 250
    #~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~#
    #~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~#

#Clan & Player Points
points:
  player-points:
    #Do you want players to get points when they kill another player? [Default value: true]
    enabled: true
    #How many points do you want the killer to get when they kill a non-enemy clan player? [Default value: 5]
    non-enemy-clan-point-amount-on-kill: 5
    #How many points do you want the killer to get when they kill an enemy clan player? [Default value: 10]
    enemy-clan-point-amount-on-kill: 10
    #Do you want to take the points given to the killer from the victims point balance if available? [Default value: true]
    take-points-from-victim: true

#Clan home system
clan-home:
  #Enable the '/clan [sethome|home]' system. [Default value: true]
  enabled: true
  cool-down:
    #Enable the cool down on the '/clan home' command to prevent tp spamming (RECOMMENDED). [Default value: true]
    enabled: true
    #Cool-down time in seconds. [Default value: 120 = 2 minutes]
    time: 120

#ClansLite update system
plugin-update-notifications:
  #Do you want to enable in game plugin update notifications? (Permission:'clanslite.update'). [Default value: true]
  enabled: true

#ClansLite general settings
general:
  run-auto-save-task:
    #Do you want to enable the plugins ability to auto save the clans to disk? [Default value: true]
    enabled: true
  run-auto-invite-wipe-task:
    #Do you want to enable the plugins ability to auto-wipe the invites list? [Default value: true]
    enabled: true
  show-auto-save-task-message:
    #Do you want to see notifications in console when the auto-save task runs? [Default value: true]
    enabled: true
  show-auto-invite-wipe-message:
    #Do you want to see notifications in console when the auto-invite-wipe task runs? [Default value: true]
    enabled: true
  developer-debug-mode:
    #Do you want to see a lot of debug messages in console when most actions are performed? [Default value: false]
    enabled: false
[/code]
[/SPOILER]

~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

[SPOILER="messages.yml"]
[code]
#~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~#
#~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~#
#                                        ----[ClansLite]----                                        #
#                                     ----[By Loving11ish]----                                      #
#                                  ----[Contributors: Gamlin]----                                   #
#~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~#
#                                  ----[Plugin Messages File]----                                   #
#~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~#
#~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~#
#ClansLite messages config for 1.3.9

#Clan Creation Messages
clan-name-too-short: "&3Clan name too short - minimum length is &6%CHARMIN% &3characters."
clan-name-too-long: "&3Clan name too long - maximum length is &6%CHARMAX% &3characters."
clan-created-successfully: "&3Clan &6%CLAN% &3was Created!"
clan-name-already-taken: "&3Sorry but &6%CLAN% &3is already taken.\n&3Please choose another!"
clan-name-is-banned: "&3Sorry but &6%CLAN% &3is a &cBANNED &3name!\n&3Please choose another!"
clan-name-cannot-contain-colours: "&3Sorry, the clan name cannot contain '&' or '#' characters."
clan-creation-failed: "&3Clan &6%CLAN% &3was NOT created, please make sure you're not already in a clan!"
clan-created-broadcast-chat: "&6%CLANOWNER%&3 Created a new clan!\n&3The new clan is called &6%CLAN%&3!"
clan-created-broadcast-title-1: "&6%CLANOWNER%&3 Created a new clan!"
clan-created-broadcast-title-2: "&3The new clan is called &6%CLAN%&3!"

#Clan Disbanded Messages
clan-successfully-disbanded: "&3Clan was disbanded!"
clan-disband-failure: "&3Failed to disband clan - Please make sure you're the owner!"
clan-admin-disband-failure: "&3Failed to disband clan - make sure that the provided player is a clan owner!"
incorrect-disband-command-usage: "&3Unrecognised argument please use &6/clanadmin disband <clan-owner>&3."

#Clan Transfer Ownership Messages
clan-ownership-transfer-successful: "&3You successfully transferred your clan to &6%PLAYER%&3."
clan-ownership-transfer-new-owner: "&6%OLDOWNER% &3has transferred ownership of &6%CLAN% &3to you."
clan-ownership-transfer-failure-owner-offline: "&3Failed to transfer clan ownership to &6%PLAYER%&3!\n&3They may be offline."
clan-ownership-transfer-failed-cannot-transfer-to-self: "&3Failed to transfer clan ownership to &6%PLAYER%!\n&3The specified player cannot be yourself!"
clan-ownership-transfer-failed-target-in-clan: "&3Failed to transfer clan as the target is already in/owns a clan!"
incorrect-clan-transfer-ownership-command-usage: "&3Unrecognised argument please use &6/clan transfer <player-name>&3."

#Clan Invite Messages
clan-invite-no-valid-player: "&3Please specify a player to invite!"
clan-invite-not-clan-owner: "&3You must be a clan owner to invite people!"
clan-invite-self-error: "&3You can't invite yourself!"
clan-invitee-not-found: "&3Player &6%INVITED% &3was not found, make sure they are online!"
clan-invite-invited-already-in-clan: "&3Player &6%INVITED% &3is already in a clan!"
clan-invite-max-size-reached: "&3You have reached the clam members size limit &a%LIMIT%&3!"
clan-invite-successful: "&3You have invited &6%INVITED% &3to your clan!"
clan-invite-failed: "&3Failed to send invite to &6%INVITED%&3, this player might already have an invitation!"
clan-invited-player-invite-pending: "&3You have been invited to a clan by &6%CLANOWNER% &3- use /clan join"
clan-invite-request: "&6%PLAYER% &3would like you to invite them to your clan.\n&3Use &6/clan invite %PLAYER% &3to send the invite."
clan-invite-request-failed: "&3Failed to send request, check that the clan owner is online!"
clan-invite-sent-successfully: "&3You have successfully sent a request to %CLANOWNER%"
clan-invite-failed-own-clan: "&3Failed to send request, this is YOUR clan!"

#Clan Join Messages
clan-join-successful: "&3Successfully joined &6%CLAN%&3!"
clan-join-failed: "&3Failed to join &6%CLAN%&3"
clan-join-failed-no-valid-clan: "&3Failed to join a clan - no clan was found!"
clan-join-failed-no-invite: "&3Failed to join a clan - no invite was found!"
clan-join-broadcast-chat: "&6%PLAYER%&3 Has joined a clan!\n&3The player's joined clan is &6%CLAN%&3!"
clan-join-broadcast-title-1: "&6%PLAYER%&3 Has joined a clan!"
clan-join-broadcast-title-2: "&3The player's joined clan is &6%CLAN%&3!"

#Clan Leave Messages
failed-clan-owner: "&3You are the owner of a clan, use &6/clan disband&3."
clan-leave-successful: "&3You have left &6%CLAN%."
clan-leave-failed: "&3Failed to leave clan, please try again later."

#Clan Kick Messages
clan-member-kick-successful: "&3Player &6%KICKEDPLAYER%&3 was kicked from your clan."
clan-kicked-player-message: "&3You were kicked from &6%CLAN%&3!"
targeted-player-is-not-in-your-clan: "&3Player &6%KICKEDPLAYER%&3 is not in your clan."
could-not-find-specified-player: "&3Could not find player &6%KICKEDPLAYER%&3. They may be have not joined before."
must-be-owner-to-kick: "&3You are not an owner of a clan!"
incorrect-kick-command-usage: "&3Unrecognised argument please use &6/clan kick <member>&3."
failed-cannot-kick-yourself: "&3You cannot kick yourself, use &6/clan leave&3."

#Clan Prefix Messages
clan-prefix-change-successful: "&3Successfully changed clan prefix to &6%CLANPREFIX%&3!"
clan-prefix-too-long: "&3Clan prefix too long - maximum length is &6%CHARMAX% &3characters."
clan-prefix-too-short: "&3Clan prefix too short - minimum length is &6%CHARMIN% &3characters."
clan-invalid-prefix: "&3Please provide a new prefix. Use &b/clan prefix <prefix>&3!"
clan-prefix-already-taken: "&3Sorry but &6%CLANPREFIX% &3is already taken.\n&3Please choose another!"
clan-prefix-is-banned: "&3Sorry but &6%CLANPREFIX% &3is a &cBANNED &3name!\n&3Please choose another!"
must-be-owner-to-change-prefix: "&3You are not an owner of a clan!"

#Clan List Messages
no-clans-to-list: "&3No clans found!\n&3Create one using &b/clan create <name>&3!"
clans-list-header: "&7----- &6ClansList &7-----&r\n&3&lCurrent clans:&r\n"
clans-list-footer: "\n&7-----"

#Clan Info Messages
clan-info-header: "&7----- &6ClanInfo &7-----&r\n&3Name: %CLAN%&r\n&3Prefix: &7(%CLANPREFIX%&r&7)&r"
clan-info-owner-online: "\n\n&3Owner: &a%OWNER%"
clan-info-owner-offline: "\n\n&3Owner: &c%OWNER%&7&o(offline)"
clan-info-members-header: "\n\n&3Members: &3&o(%NUMBER%)"
clan-info-members-online: "\n&a%MEMBER%\n"
clan-info-members-offline: "\n&c%MEMBER%&7&o(offline)\n"
clan-info-allies-header: "\n\n&3Allied Clans:"
clan-ally-members: "\n&a%ALLYCLAN%\n"
clan-ally-members-not-found: "\n&aAlly not found\n"
clan-info-enemies-header: "\n\n&3Enemy Clans:"
clan-enemy-members: "\n&c%ENEMYCLAN%\n"
clan-enemy-members-not-found: "\n&aEnemy not found\n"
clan-pvp-status-enabled: "\n\n&3Friendly Fire: &a&oENABLED"
clan-pvp-status-disabled: "\n\n&3Friendly Fire: &c&oDISABLED"
clan-home-set-true: "\n\n&3Home Set: &a&oTRUE"
clan-home-set-false: "\n\n&3Home Set: &c&oFALSE"
clan-points-value: "\n\n&3Clan Points Balance: &a%POINTS%"
clan-info-footer: "\n&7-----"
not-in-clan: "&3You are not in a clan!"

#Clan Ally Messages
added-clan-to-your-allies: "&3You successfully added &6%ALLYCLAN% &3to your allies!"
clan-added-to-other-allies: "&6%CLANOWNER% &3has added your clan to their allies!"
failed-to-add-clan-to-allies: "&3Unable to add &6%ALLYOWNER%&3! Make sure the owner is online!"
failed-clan-already-your-ally: "&3This clan is already your ally!"
failed-player-not-clan-owner: "&6%ALLYOWNER%&3 is not a clan owner!"
removed-clan-from-your-allies: "&3You Successfully removed &6%ALLYCLAN% &3from your allies!"
clan-removed-from-other-allies: "&3The clan owner &6%CLANOWNER% &3has removed your clan from their allies!"
failed-to-remove-clan-from-allies: "&3Unable to remove &6%ALLYOWNER%&3! Make sure they're you ally"
ally-clan-add-owner-offline: "&3Unable to add &6%ALLYOWNER%&3! Make sure the owner is online!"
ally-clan-remove-owner-offline: "&3Unable to remove &6%ALLYOWNER%&3! Make sure the owner is online!"
incorrect-clan-ally-command-usage: "&3Unrecognised argument please use &6/clan ally [add|remove] <clan-owner>&3."
failed-cannot-ally-your-own-clan: "&3You cannot be an ally with your &3&lOWN&r&3 clan!"
clan-ally-max-amount-reached: "&3You have reached the clan allies amount limit &a%LIMIT%&3!"
failed-cannot-ally-enemy-clan: "&3You cannot be an ally with an &c&lENEMY&r &3clan!"

#Clan Enemy Messages
added-clan-to-your-enemies: "&3You successfully added &6%ENEMYCLAN% &3to your enemies!"
clan-added-to-other-enemies: "&6%CLANOWNER% &3has added your clan to their enemies!"
failed-to-add-clan-to-enemies: "&3Unable to add &6%ENEMYOWNER%&3! Make sure the owner is online!"
failed-clan-already-your-enemy: "&3This clan is already your enemy!"
failed-enemy-player-not-clan-owner: "&6%ENEMYOWNER%&3 is not a clan owner!"
removed-clan-from-your-enemies: "&3You Successfully removed &6%ENEMYCLAN% &3from your enemies!"
clan-removed-from-other-enemies: "&3The clan owner &6%CLANOWNER% &3has removed your clan from their enemies!"
failed-to-remove-clan-from-enemies: "&3Unable to remove &6%ENEMYOWNER%&3! Make sure they're your enemy!"
enemy-clan-add-owner-offline: "&3Unable to add &6%ENEMYOWNER%&3! Make sure the owner is online!"
enemy-clan-remove-owner-offline: "&3Unable to remove &6%ENEMYOWNER%&3! Make sure the owner is online!"
incorrect-clan-enemy-command-usage: "&3Unrecognised argument please use &6/clan enemy [add|remove] <clan-owner>&3."
failed-cannot-enemy-your-own-clan: "&3You cannot be an enemy with your &3&lOWN&r&3 clan!"
clan-enemy-max-amount-reached: "&3You have reached the clan enemies amount limit &a%LIMIT%&3!"
failed-cannot-enemy-allied-clan: "&3You cannot be an enemy with an &a&lALLIED&r &3clan!"
added-enemy-clan-to-your-enemies-title-1: "&c&lYOUR CLAN IS NOW AT WAR WITH:"
added-enemy-clan-to-your-enemies-title-2: "&6%CLANOWNER%'s &cClan!"
removed-enemy-clan-from-your-enemies-title-1: "&a&lYOUR CLAN IS NO LONGER AT WAR WITH:"
removed-enemy-clan-from-your-enemies-title-2: "&6%CLANOWNER%'s &aClan!"
clan-added-to-other-enemies-title-1: "&c&lYOUR CLAN IS NOW AT WAR WITH:"
clan-added-to-other-enemies-title-2: "&6%CLANOWNER%'s &cClan!"
clan-removed-from-other-enemies-title-1: "&a&lYOUR CLAN IS NO LONGER AT WAR WITH:"
clan-removed-from-other-enemies-title-2: "&6%CLANOWNER%'s &aClan!"

#Clan Friendly Fire
enabled-friendly-fire: "&3You successfully &aenabled &3friendly fire.\n&3Your clan members can now pvp each other!"
disabled-friendly-fire: "&3You successfully &cdisabled &3friendly fire.\n&3Your clan members can no longer pvp each other!"
failed-not-in-clan: "&3You need to be in a clan first! Use &6/clan &3for details how."
friendly-fire-is-disabled: "&3Your clan has friendly fire disabled."

#Clan Chest Protections
chest-purchased-successfully: "&3You have &aSUCCESSFULLY &3purchased &6%AMOUNT% &3new clan chest/s."
chest-protected-successfully: "&3You have &aSUCCESSFULLY &3protected a new chest at: &6%X%, %Y%, %Z%&3."
chest-protection-removed-successfully: "&3You have &aSUCCESSFULLY &3removed a protected chest at: &6%X%, %Y%, %Z%&3."
block-targeted-incorrect-material: "&3Please look at a SINGLE chest block to use this command!"
chest-max-amount-reached: "&3You have reached the protected chest size limit for your clan. Your limit is: &a%LIMIT% &3chests!"
chest-owned-by-another-clan: "&3Sorry, that chest is already owned by &6%CLAN%&3's clan."
chest-owned-by-another-clan-name-unknown: "&3Sorry, that chest is already owned by another clan."
failed-chest-already-protected: "&3That chest is already protected!"
failed-chest-not-protected: "&3Sorry, you don't own that chest or it is not locked."
failed-not-enough-points: "&3Sorry, your clan doesn't have enough points to purchase &6%AMOUNT% &3new lock/s."
players-with-access-list:
  header: "&7----- &6Players With Access To Chest &7-----&r\n"
  player-entry: "  &7>> &e%PLAYER%&7\n"
  footer: "&7----- &6Players With Access To Chest &7-----&r\n"

#Clan Homes
successfully-set-clan-home: "&3You &aSuccessfully &3set the clan home to your current location!"
successfully-deleted-clan-home: "&3You &aSuccessfully &3deleted the clan home!"
home-cool-down-timer-wait: "&3Sorry, you can't use that again for another &6%TIMELEFT%&r &3seconds."
successfully-teleported-to-home: "&3You teleported to your clan's home."
failed-no-home-set: "&3Your clan does not have a home set!"
failed-tp-not-in-clan: "&3You need to be in a clan first! Use &6/clan &3for details how."

#Clan Chat
failed-must-be-in-clan: "&3You need to be in a clan first! Use &6/clan &3for details how."
chat-cool-down-timer-wait: "&3Sorry, you can't use that again for another &6%TIMELEFT%&r &3seconds."
chatspy-toggle-on: "&3Toggled Clan Chat spy &a&lON&3."
chatspy-toggle-off: "&3Toggled Clan Chat spy &c&lOFF&3."

#Clan & Player Points
player-points-killer-non-enemy-received-success: "&3You killed &6%PLAYER% &3and won &6%POINTVALUE%&3 points!"
player-points-killer-enemy-received-success: "&3You killed &6%PLAYER% &3,they were a clan enemy and you won &6%ENEMYPOINTVALUE%&3 points!"
player-points-victim-non-enemy-withdrawn-success: "&3You died to &6%KILLER% &3and had &6%POINTVALUE% &3points withdrawn!"
player-points-victim-enemy-withdrawn-success: "&3You died to &6%KILLER% &3,they were a clan enemy and you had &6%ENEMYPOINTVALUE% &3points withdrawn!"
player-points-console-victim-point-withdraw-failed: "&6%VICTIM% &cdid not have enough points to remove when they were killed!"
player-points-victim-withdraw-failed: "&3You did not have enough points to be withdrawn when you died!"
player-points-list-command: "&3You currently have &6%POINTVALUE% &3points in your personal balance."
clan-deposit-points-success: "&3You successfully deposited &6%POINTS% &3points into &6%CLAN%'s &3account."
clan-deposit-points-failed: "&cFailed to deposit &6%POINTS% &cpoints into &6%CLAN%'s &caccount. &3You have insufficient points!"
clan-deposit-points-invalid-point-amount: "&cPlease provide an amount to deposit!"
clan-deposit-points-incorrect-command: "&cPlease provide a point value: &6/clan point deposit <amount>"
clan-withdraw-points-success: "&3You successfully withdrew &6%POINTS% &3from &6%CLAN%'s &3into your account."
clan-withdraw-points-failed: "&cFailed to withdraw &6%POINTS% &cfrom &6%CLAN%'s &caccount. &3Your clan has insufficient points!"
clan-withdraw-points-invalid-point-amount: "&cPlease provide an amount to withdraw!"
clan-withdraw-points-incorrect-command: "&cPlease provide a point value: &6/clan point withdraw <amount>"
clan-points-failed-not-in-clan: "&3You need to be in a clan first! Use &6/clan &3for details how."

#PlayerPoints command
all-points-list-header: "&7----- &6All Player Points &7-----&r\n"
all-points-list-entry: " &7>> &e%PLAYER%&7: &6%POINTVALUE%\n"
all-points-list-footer: "&7----- &6All Player Points &7-----&r"

#General Plugin Messages
clan-must-be-owner: "&3You must be the clan owner to do this."
function-disabled: "&3Sorry, that has been disabled. :("
no-permission: "&6ClansLite: &cSorry, you don't have permission to do that. :("
plugin-reload-begin: "&6ClansLite: &aBeginning plugin reload..."
plugin-reload-successful: "&6ClansLite: &aThe plugin has been successfully reloaded!"
incorrect-command-usage: "&3Unrecognised argument please use &6/clan&3."
player-only-command: "&4Sorry, that command can only be run by a player!"
chest-location-save-failed-1: "&4The protected chest for &6%CLAN% &4could not be saved as the world value was null!"
chest-location-save-failed-2: "&cSkipping to next stored protected chest for &6%CLAN%&c!"
chest-location-load-failed: "&4The world &6%WORLD% &4could not be found! Skipping current protected chest for &6%CLAN%&4!"
saving-clans-start: "&6ClansLite: Saving clans data..."
save-completed: "&6ClansLite: &aClans saved!"
save-failed-no-clans: "&6ClansLite: &cSkipping saving of clans as &c&lNO &cclans are stored in memory!"
auto-save-started: "&6ClansLite: &aAuto save task has started."
auto-save-complete: "&6ClansLite: &aSaved all Clans to file!"
auto-save-failed: "&6ClansLite: &4Failed to save clans.yml to file!"
auto-invite-wipe-started: "&6ClansLite: &aAuto invite wipe task has started."
auto-invite-wipe-complete: "&6ClansLite: &aCleared all outstanding clan invites!"
invite-wipe-failed: "&6ClansLite: &cFailed to clear all outstanding clan invites!"
clans-save-error-1: "&6ClansLite: &4Failed to save clans.yml to file!"
clans-save-error-2: "&6ClansLite: &4Check the console for errors!"
clans-load-error-1: "&6ClansLite: &4Failed to load data from clans.yml!"
clans-load-error-2: "&6ClansLite: &4See console for errors!"
clans-update-error-1: "&6ClansLite: &4Failed to update clans.yml file!"
clans-update-error-2: "&6ClansLite: &4Check the console for errors!"
clan-player-not-found-1: "&6ClansLite: &4Failed to find player by name of &6%PLAYER%&4!"
clan-player-not-found-2: "&6ClansLite: &4Ensure that &6%PLAYER% &4has joined before!"

#/chest Command Responses
chest-command-incorrect-usage:
  line-1: "&6ClansLite chest protections usage:"
  line-2: "&3/chest lock"
  line-3: "&3/chest unlock"
  line-4: "&3/chest buylock <amount>"
  line-5: "&3/chest accesslist"

#/clan Command Responses
clan-command-incorrect-usage:
  line-1: "&6ClansLite usage:"
  line-2: "&3/clan create <name>"
  line-3: "&3/clan disband"
  line-4: "&3/clan invite <player>"
  line-5: "&3/clan join"
  line-6: "&3/clan leave"
  line-7: "&3/clan kick <player>"
  line-8: "&3/clan info"
  line-9: "&3/clan list"
  line-10: "&3/clan prefix <prefix>"
  line-11: "&3/clan transfer <player-name>"
  line-12: "&3/clan ally [add|remove] <clan-owner>"
  line-13: "&3/clan enemy [add|remove] <clan-owner>"
  line-14: "&3/clan pvp"
  line-15: "&3/clan [sethome|delhome|home]"
  line-16: "&3/clan points [deposit|withdraw] <amount>"

#/clanadmin Command Responses
clanadmin-command-incorrect-usage:
  line-1: "&6ClansLite Admin usage:"
  line-2: "&3/clanadmin save"
  line-3: "&3/clanadmin reload"
  line-4: "&3/clanadmin disband <clan-owner>"
  line-5: "&3/clanadmin about"

#Update Notification
update-check-failure: "&6ClansLite: &4Unable to check for updates! - &c"
update-available:
  1: "&4*-------------------------------------------*"
  2: "&6ClansLite: &cA new version is available!"
  3: "&4*-------------------------------------------*"
no-update-available:
  1: "&a*-------------------------------------------*"
  2: "&6ClansLite: &aPlugin is up to date!"
  3: "&a*-------------------------------------------*"
[/code]
[/SPOILER]

~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

[SPOILER="clangui.yml"]
[code]
#~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~#
#~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~#
#                                        ----[ClansLite]----                                        #
#                                     ----[By Loving11ish]----                                      #
#                                  ----[Contributors: Gamlin]----                                   #
#~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~#
#                                 ----[Plugin GUI Config File]----                                  #
#~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~#
#~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~#
#ClansLite gui config for 1.3.9

#This is the options for the ClanList GUI
clan-list:
  #What name would you like the gui to have?
  name: "&3Clans List"

  #Message sent when your on the first page.
  GUI-first-page: "&7You are on the first page."
  #Message sent when your on the last page.
  GUI-last-page: "&7You are on the last page."

  #Menu controls options
  menu-controls:
    #The name of the previous page icon
    previous-page-icon-name: "&2Previous Page"
    #The name of the next page icon
    next-page-icon-name: "&2Next Page"
    #The name of the close/go back icon
    close-go-back-icon-name: "&4Close/Go Back"

  icons:
    #Do you want to use the auto data update system for the icons in the GUI? [Default value: true]
    ##Disabling this option may help performance on lower spec servers!##
    auto-refresh-data:
      enabled: true

    icon-display-name:
      #Do you want to use the Clan Name as the title for the icon: [Default value: true]
      #If below is 'false' the name will be empty.
      use-clan-name: true

    #This allows you to customise the lore text for the player head icons in the ClanList GUI.
    lore:
      header: "&7----------"
      prefix: "&3Clan Prefix: "
      owner-online: "&3Clan Owner: &d"
      owner-offline: "&3Clan Owner &7&o(Offline)&3: &d"
      members: "&3Clan Members:"
      allies: "&3Clan Allies:"
      enemies: "&3Clan Enemies:"
      footer-1: "&7----------"
      action: "&d&oClick to send an invite request to this clan owner if online"
      footer-2: "&7----------"

#This is the options for the ClanJoinRequest GUI
clan-join:
  #What name would you like the gui to have?
  name: "&3Ask to join Clan?"

  icons:
    #This allows you to customise the name text for the icons in the ClanJoinRequest GUI.
    send-request-name: "&a&oSend request to join?"
    cancel-request-name: "&c&oCancel and go back"
[/code]
[/SPOILER]

~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

Chat prefix
ClansLite exposes a variable of {CLAN} to use in Essentials Chat or similar.

Simply add the `{CLAN}` placeholder into the `chat.format` string in the EssentialsX config.yml in the location you'd like it to appear in your chat.
Reload EssentialsX or restart the server and the clan prefix should appear like magic!

~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

Placeholder API
ClansLite exposes 11 external placeholders using PlaceholderAPI to enable the fetching of a players clan name, prefix, friendly fire status and home set.

See documentation tab for list of available placeholders.

~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
Please report any issues and feel free to join my [discord] (https://discord.gg/crapticraft)
###Thank you for using my plugin!
