package pinacolada.commands;

import basemod.DevConsole;
import basemod.devcommands.ConsoleCommand;
import basemod.helpers.ConvertHelper;
import pinacolada.augments.PCLAugment;
import pinacolada.augments.PCLAugmentData;
import pinacolada.resources.PGR;

import java.util.ArrayList;

public class AugmentCommand extends ConsoleCommand
{

    public AugmentCommand()
    {
        this.requiresPlayer = true;
        this.minExtraTokens = 2;
        this.simpleCheck = true;
    }

    public static ArrayList<String> getCustoms()
    {
        return new ArrayList<>(PCLAugment.getIDs());
    }

    @Override
    protected void execute(String[] tokens, int depth)
    {
        PCLAugmentData augment = PCLAugment.get(tokens[1]);
        Integer amount = ConvertHelper.tryParseInt(tokens[2], 1);
        if (augment != null)
        {
            PGR.core.dungeon.addAugment(augment.ID, amount);
            DevConsole.log("Obtained " + amount + " " + tokens[1]);
        }
        else
        {
            DevConsole.log("Could not find augment " + tokens[1]);
        }
    }

    public ArrayList<String> extraOptions(String[] tokens, int depth)
    {
        ArrayList<String> options = getCustoms();
        if (options.contains(tokens[depth]))
        {
            if (tokens.length > depth + 1 && tokens[depth + 1].matches("\\d*"))
            {
                return ConsoleCommand.smallNumbers();
            }
        }

        return options;
    }
}
