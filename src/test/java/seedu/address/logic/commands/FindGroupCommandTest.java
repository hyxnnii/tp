package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.Messages.MESSAGE_GROUPS_LISTED_OVERVIEW;
import static seedu.address.logic.Messages.MESSAGE_PERSONS_LISTED_OVERVIEW;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.ExitCommand.MESSAGE_EXIT_ACKNOWLEDGEMENT;
import static seedu.address.testutil.TypicalGroups.GOONERS;
import static seedu.address.testutil.TypicalPersons.CARL;
import static seedu.address.testutil.TypicalPersons.ELLE;
import static seedu.address.testutil.TypicalPersons.FIONA;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.group.Group;
import seedu.address.model.group.GroupContainsKeywordsPredicate;
import seedu.address.model.person.NameContainsKeywordsPredicate;

/**
 * Contains integration tests (interaction with the Model) for {@code FindCommand}.
 */
public class FindGroupCommandTest {
    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    private Model expectedModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void equals() {

        GroupContainsKeywordsPredicate firstGroupPredicate =
                new GroupContainsKeywordsPredicate(Collections.singletonList("firstGroup"));
        GroupContainsKeywordsPredicate secondGroupPredicate =
                new GroupContainsKeywordsPredicate(Collections.singletonList("secondGroup"));

        FindGroupCommand findFirstGroupCommand = new FindGroupCommand(firstGroupPredicate);
        FindGroupCommand findSecondGroupCommand = new FindGroupCommand(secondGroupPredicate);
        FindGroupCommand findFirstGroupCommandCopy = new FindGroupCommand(firstGroupPredicate);

        // Same object -> returns true
        assertTrue(findFirstGroupCommand.equals(findFirstGroupCommand));

        // Same predicate -> returns true
        assertTrue(findFirstGroupCommand.equals(findFirstGroupCommandCopy));

        // Different predicate -> returns false
        assertFalse(findFirstGroupCommand.equals(findSecondGroupCommand));

        // Different object type -> returns false
        assertFalse(findFirstGroupCommand.equals("notACommand"));

        // Null comparison -> returns false
        assertFalse(findFirstGroupCommand.equals(null));
    }

    @Test
    public void execute_zeroKeywords_noGroupFound() {
        String expectedMessage = String.format(MESSAGE_GROUPS_LISTED_OVERVIEW, 0);
        GroupContainsKeywordsPredicate predicate = prepareGroupPredicate(" ");
        FindGroupCommand command = new FindGroupCommand(predicate);
        expectedModel.updateFilteredGroupList(predicate);
        CommandResult expectedCommandResult = new CommandResult(expectedMessage, false, true, false);
        assertCommandSuccess(command, model, expectedCommandResult, expectedModel);
        assertEquals(Collections.emptyList(), model.getFilteredGroupList());
    }
    @Test
    public void execute_findGroup() {
        model.addGroup(GOONERS);
        expectedModel.addGroup(GOONERS);

        GroupContainsKeywordsPredicate groupPredicate = prepareGroupPredicate("gooners");

        FindGroupCommand command = new FindGroupCommand(groupPredicate);

        model.updateFilteredGroupList(groupPredicate);

        expectedModel.updateFilteredGroupList(groupPredicate);

        // Calculate the expected message based on the number of persons in the filtered list
        String expectedMessage = String.format(MESSAGE_GROUPS_LISTED_OVERVIEW,
                expectedModel.getFilteredGroupList().size());

        CommandResult expectedCommandResult = new CommandResult(expectedMessage, false, true, false);
        assertCommandSuccess(command, model, expectedCommandResult, expectedModel);

    }


    @Test
    public void toStringMethod() {
        GroupContainsKeywordsPredicate predicate = new GroupContainsKeywordsPredicate(Arrays.asList("keyword"));
        FindGroupCommand findGroupCommand = new FindGroupCommand(predicate);
        String expected = FindGroupCommand.class.getCanonicalName() + "{groupPredicate=" + predicate + "}";
        assertEquals(expected, findGroupCommand.toString());
    }

    /**
     * Parses {@code userInput} into a {@code GroupContainsKeywordsPredicate}.
     */
    private GroupContainsKeywordsPredicate prepareGroupPredicate(String userInput) {
        return new GroupContainsKeywordsPredicate(Arrays.asList(userInput.split("\\s+")));
    }
}
