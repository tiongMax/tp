package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.showVolunteerAtIndex;
import static seedu.address.testutil.TypicalEvents.getTypicalEventStorage;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND;
import static seedu.address.testutil.TypicalVolunteers.getTypicalVolunteerStorage;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.volunteercommands.VolunteerDeleteCommand;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.volunteer.Volunteer;

/**
 * Contains integration tests (interaction with the Model) and unit tests for
 * {@code VolunterDeleteCommand}.
 */
public class VolunteerDeleteCommandTest {

    private Model model = new ModelManager(getTypicalEventStorage(), getTypicalVolunteerStorage(), new UserPrefs());

    @Test
    public void execute_validIndexUnfilteredList_success() {
        Volunteer volunteerToDelete = model.getFilteredVolunteerList().get(INDEX_FIRST.getZeroBased());
        VolunteerDeleteCommand volunteerDeleteCommand = new VolunteerDeleteCommand(INDEX_FIRST);

        String expectedMessage = String.format(VolunteerDeleteCommand.MESSAGE_DELETE_VOLUNTEER_SUCCESS,
                Messages.format(volunteerToDelete));

        ModelManager expectedModel = new ModelManager(model.getEventStorage(), model.getVolunteerStorage(),
                                                        new UserPrefs());
        expectedModel.deleteVolunteer(volunteerToDelete);

        assertCommandSuccess(volunteerDeleteCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexUnfilteredList_throwsCommandException() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredVolunteerList().size() + 1);
        VolunteerDeleteCommand volunteerDeleteCommand = new VolunteerDeleteCommand(outOfBoundIndex);

        assertCommandFailure(volunteerDeleteCommand, model, Messages.MESSAGE_INVALID_VOLUNTEER_DISPLAYED_INDEX);
    }

    @Test
    public void execute_validIndexFilteredList_success() {
        showVolunteerAtIndex(model, INDEX_FIRST);

        Volunteer volunteerToDelete = model.getFilteredVolunteerList().get(INDEX_FIRST.getZeroBased());
        VolunteerDeleteCommand volunteerDeleteCommand = new VolunteerDeleteCommand(INDEX_FIRST);

        String expectedMessage = String.format(VolunteerDeleteCommand.MESSAGE_DELETE_VOLUNTEER_SUCCESS,
                Messages.format(volunteerToDelete));

        Model expectedModel = new ModelManager(model.getEventStorage(), model.getVolunteerStorage(), new UserPrefs());
        expectedModel.deleteVolunteer(volunteerToDelete);
        showNoVolunteer(expectedModel);

        assertCommandSuccess(volunteerDeleteCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexFilteredList_throwsCommandException() {
        showVolunteerAtIndex(model, INDEX_FIRST);

        Index outOfBoundIndex = INDEX_SECOND;
        // ensures that outOfBoundIndex is still in bounds of address book list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getVolunteerStorage().getVolunteerList().size());

        VolunteerDeleteCommand volunteerDeleteCommand = new VolunteerDeleteCommand(outOfBoundIndex);

        assertCommandFailure(volunteerDeleteCommand, model, Messages.MESSAGE_INVALID_VOLUNTEER_DISPLAYED_INDEX);
    }

    @Test
    public void equals() {
        VolunteerDeleteCommand deleteFirstCommand = new VolunteerDeleteCommand(INDEX_FIRST);
        VolunteerDeleteCommand deleteSecondCommand = new VolunteerDeleteCommand(INDEX_SECOND);

        // same object -> returns true
        assertTrue(deleteFirstCommand.equals(deleteFirstCommand));

        // same values -> returns true
        VolunteerDeleteCommand deleteFirstCommandCopy = new VolunteerDeleteCommand(INDEX_FIRST);
        assertTrue(deleteFirstCommand.equals(deleteFirstCommandCopy));

        // different types -> returns false
        assertFalse(deleteFirstCommand.equals(1));

        // null -> returns false
        assertFalse(deleteFirstCommand.equals(null));

        // different volunteer -> returns false
        assertFalse(deleteFirstCommand.equals(deleteSecondCommand));
    }

    @Test
    public void toStringMethod() {
        Index targetIndex = Index.fromOneBased(1);
        VolunteerDeleteCommand volunteerDeleteCommand = new VolunteerDeleteCommand(targetIndex);
        String expected = VolunteerDeleteCommand.class.getCanonicalName() + "{targetIndex=" + targetIndex + "}";
        assertEquals(expected, volunteerDeleteCommand.toString());
    }

    /**
     * Updates {@code model}'s filtered list to show no one.
     */
    private void showNoVolunteer(Model model) {
        model.updateFilteredVolunteerList(p -> false);

        assertTrue(model.getFilteredVolunteerList().isEmpty());
    }
}
