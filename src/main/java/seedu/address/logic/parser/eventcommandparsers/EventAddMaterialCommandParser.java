package seedu.address.logic.parser.eventcommandparsers;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EVENT_ID;
import static seedu.address.logic.parser.CliSyntax.PREFIX_MATERIAL;

import java.util.stream.Stream;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.eventcommands.EventAddMaterialCommand;
import seedu.address.logic.parser.ArgumentMultimap;
import seedu.address.logic.parser.ArgumentTokenizer;
import seedu.address.logic.parser.Parser;
import seedu.address.logic.parser.ParserUtil;
import seedu.address.logic.parser.Prefix;
import seedu.address.logic.parser.exceptions.ParseException;


/**
 * Parses input arguments and creates a new EventCreateCommand object
 */
public class EventAddMaterialCommandParser implements Parser<EventAddMaterialCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the EventAddMaterialCommand
     * and returns an EventAddMaterialCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public EventAddMaterialCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_EVENT_ID, PREFIX_MATERIAL);

        if (!arePrefixesPresent(argMultimap, PREFIX_EVENT_ID, PREFIX_MATERIAL)
                || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    EventAddMaterialCommand.MESSAGE_USAGE));
        }

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_EVENT_ID, PREFIX_MATERIAL);
        try {
            Index eventIndex = ParserUtil.parseIndex(argMultimap.getValue(PREFIX_EVENT_ID).get());
            String materialName = ParserUtil.parseMaterialName(argMultimap.getValue(PREFIX_MATERIAL).get());
            int amount = ParserUtil.parseMaterialQuantity(argMultimap.getValue(PREFIX_MATERIAL).get());

            return new EventAddMaterialCommand(eventIndex, amount, materialName);
        } catch (ParseException pe) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, EventAddMaterialCommand.MESSAGE_USAGE), pe);
        }
    }

    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }

}
