package dev.jadss.jadgens.api.config.generalConfig.messages;

import dev.jadss.jadgens.api.config.generalConfig.messages.commands.*;
import dev.jadss.jadgens.api.config.generalConfig.messages.menu.DropsMenuConfiguration;
import dev.jadss.jadgens.api.config.generalConfig.messages.menu.GiveMenuConfiguration;
import dev.jadss.jadgens.api.config.generalConfig.messages.menu.MachineMenuConfiguration;
import dev.jadss.jadgens.api.config.generalConfig.messages.menu.ShopMenuConfiguration;
import dev.jadss.jadgens.api.config.interfaces.Configuration;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class MessagesConfiguration implements Configuration {

    //Commands!
    public final HelpCommandMessagesConfiguration helpCommand;
    public final GiveCommandMessagesConfiguration giveCommand;
    public final ActionsCommandMessagesConfiguration actionsCommand;
    public final ShopCommandMessagesConfiguration shopCommand;
    public final InfoCommandMessagesConfiguration infoCommand;

    //Menus!
    public final MachineMenuConfiguration machineMenu;
    public final ShopMenuConfiguration shopMenu;
    public final DropsMenuConfiguration dropsMenu;
    public final GiveMenuConfiguration giveMenu;

    //Messages.
    public final MachineMessagesConfiguration machineMessages;
    public final FuelMessagesConfiguration fuelMessages;
    public final ShopMessagesConfiguration shopMessages;
    public final DropsMessageConfiguration dropsMessages;

    public final GlobalMessagesConfiguration globalMessages;

    public MessagesConfiguration() {
        this(null, null, null, null, null, null, null, null, null, null, null, null, null, null);
    }
}
