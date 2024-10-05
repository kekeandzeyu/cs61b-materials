import jh61b.utils.Reflection;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.google.common.truth.Truth.assertThat;
import static com.google.common.truth.Truth.assertWithMessage;

/** Performs some basic linked list tests. */
public class LinkedListDeque61BTest {

     @Test
     /** In this test, we have three different assert statements that verify that addFirst works correctly. */
     public void addFirstTestBasic() {
         Deque61B<String> lld1 = new LinkedListDeque61B<>();

         lld1.addFirst("back"); // after this call we expect: ["back"]
         assertThat(lld1.toList()).containsExactly("back").inOrder();

         lld1.addFirst("middle"); // after this call we expect: ["middle", "back"]
         assertThat(lld1.toList()).containsExactly("middle", "back").inOrder();

         lld1.addFirst("front"); // after this call we expect: ["front", "middle", "back"]
         assertThat(lld1.toList()).containsExactly("front", "middle", "back").inOrder();

         /* Note: The first two assertThat statements aren't really necessary. For example, it's hard
            to imagine a bug in your code that would lead to ["front"] and ["front", "middle"] failing,
            but not ["front", "middle", "back"].
          */
     }

     @Test
     /** In this test, we use only one assertThat statement. IMO this test is just as good as addFirstTestBasic.
      *  In other words, the tedious work of adding the extra assertThat statements isn't worth it. */
     public void addLastTestBasic() {
         Deque61B<String> lld1 = new LinkedListDeque61B<>();

         lld1.addLast("front"); // after this call we expect: ["front"]
         lld1.addLast("middle"); // after this call we expect: ["front", "middle"]
         lld1.addLast("back"); // after this call we expect: ["front", "middle", "back"]
         assertThat(lld1.toList()).containsExactly("front", "middle", "back").inOrder();
     }

     @Test
     /** This test performs interspersed addFirst and addLast calls. */
     public void addFirstAndAddLastTest() {
         Deque61B<Integer> lld1 = new LinkedListDeque61B<>();

         /* I've decided to add in comments the state after each call for the convenience of the
            person reading this test. Some programmers might consider this excessively verbose. */
         lld1.addLast(0);   // [0]
         lld1.addLast(1);   // [0, 1]
         lld1.addFirst(-1); // [-1, 0, 1]
         lld1.addLast(2);   // [-1, 0, 1, 2]
         lld1.addFirst(-2); // [-2, -1, 0, 1, 2]

         assertThat(lld1.toList()).containsExactly(-2, -1, 0, 1, 2).inOrder();
     }

    // Below, you'll write your own tests for LinkedListDeque61B.

    @Test
    @DisplayName("Check that addFirst works on an empty deque and on a non-empty deque.")
    public void addFirstTest() {
        Deque61B<Integer> lld1 = new LinkedListDeque61B<>();
        lld1.addFirst(0);
        assertThat(lld1.toList()).containsExactly(0).inOrder();
        lld1.addFirst(1);
        lld1.addFirst(2);
        assertThat(lld1.toList()).containsExactly(2, 1, 0).inOrder();
    }

    @Test
    @DisplayName("Check that addLast works on an empty deque and on a non-empty deque.")
    public void addLastTest() {
        Deque61B<Integer> lld1 = new LinkedListDeque61B<>();
        lld1.addLast(0);
        assertThat(lld1.toList()).containsExactly(0).inOrder();
        lld1.addLast(1);
        lld1.addLast(2);
        assertThat(lld1.toList()).containsExactly(0, 1, 2).inOrder();
    }

    @Test
    @DisplayName("add_first_after_remove_to_empty: Add some elements to a deque and remove them all, then check that addFirst still works.")
    public void addFirstAdvancedTest() {
        Deque61B<String> express = new LinkedListDeque61B<>();
        express.addFirst("Had");
        express.addFirst("I");
        express.addFirst("not");
        assertWithMessage("addFirst is not working correctly").that(express.toList())
                .containsExactly("not", "I", "Had").inOrder();
        for (int i = 0; i < 3; i++) {
            express.removeFirst();
        }
        assertWithMessage("addFirst is not working correctly").that(express.toList()).isEmpty();
        express.addFirst("Congratulations!");
        assertThat(express.toList()).containsExactly("Congratulations!").inOrder();
    }

    @Test
    @DisplayName("add_last_after_remove_to_empty: Add some elements to a deque and remove them all, then check that addLast still works.")
    public void addLastAdvancedTest() {
        Deque61B<String> express = new LinkedListDeque61B<>();
        express.addLast("seen");
        express.addLast("the");
        express.addLast("sun");
        assertWithMessage("addLast is not working correctly").that(express.toList())
                .containsExactly("seen", "the", "sun").inOrder();
        for (int i = 0; i < 3; i++) {
            express.removeLast();
        }
        assertWithMessage("addLast is not working correctly").that(express.toList()).isEmpty();
        express.addLast("Congratulations!");
        assertThat(express.toList()).containsExactly("Congratulations!").inOrder();
    }

    @Test
    @DisplayName("get_valid: Check that get works on a valid index.")
    public void testGetValid() {
        Deque61B<Integer> lld = new LinkedListDeque61B<>();
        lld.addLast(1);
        lld.addLast(2);
        lld.addLast(3);
        assertThat(lld.get(1)).isEqualTo(2);
    }

    @Test
    @DisplayName("get_oob_large: Check that get works on a large, out of bounds index.")
    public void testGetOutOfBoundsLarge() {
        Deque61B<Integer> lld = new LinkedListDeque61B<>();
        lld.addLast(1);
        lld.addLast(2);
        assertThat(lld.get(2)).isNull();
    }

    @Test
    @DisplayName("get_oob_neg: Check that get works on a negative index.")
    public void testGetOutOfBoundsNegative() {
        Deque61B<Integer> lld = new LinkedListDeque61B<>();
        lld.addLast(1);
        lld.addLast(2);
        assertThat(lld.get(-1)).isNull();
    }

    @Test
    @DisplayName("get_recursive_valid: Check that getRecursive works on a valid index.")
    public void testGetRecursiveValid() {
        Deque61B<Integer> lld = new LinkedListDeque61B<>();
        lld.addLast(1);
        lld.addLast(2);
        lld.addLast(3);
        assertThat(lld.getRecursive(1)).isEqualTo(2);
    }

    @Test
    @DisplayName("get_recursive_oob_large: Check that getRecursive works on a large, out of bounds index.")
    public void testGetRecursiveOutOfBoundsLarge() {
        Deque61B<Integer> lld = new LinkedListDeque61B<>();
        lld.addLast(1);
        lld.addLast(2);
        assertThat(lld.getRecursive(2)).isNull();
    }

    @Test
    @DisplayName("get_recursive_oob_neg: Check that getRecursive works on a negative index.")
    public void testGetRecursiveOutOfBoundsNegative() {
        Deque61B<Integer> lld = new LinkedListDeque61B<>();
        lld.addLast(1);
        lld.addLast(2);
        assertThat(lld.getRecursive(-1)).isNull();
    }

    // Flags for size tests
    @Test
    @DisplayName("size: Check that size works.")
    public void testSize() {
        Deque61B<Integer> lld = new LinkedListDeque61B<>();
        lld.addLast(1);
        lld.addLast(2);
        assertThat(lld.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("size_after_remove_to_empty: Add some elements to a deque and remove them all, then check that size still works.")
    public void testSizeAfterRemoveToEmpty() {
        Deque61B<Integer> lld = new LinkedListDeque61B<>();
        lld.addLast(1);
        lld.addLast(2);
        lld.removeFirst();
        lld.removeLast();
        assertThat(lld.size()).isEqualTo(0);
    }

    @Test
    @DisplayName("size_after_remove_from_empty: Remove from an empty deque, then check that size still works.")
    public void testSizeAfterRemoveFromEmpty() {
        Deque61B<Integer> lld = new LinkedListDeque61B<>();
        lld.removeFirst(); // Removing from an empty deque
        assertThat(lld.size()).isEqualTo(0);
    }

    // Flags for isEmpty tests
    @Test
    @DisplayName("is_empty_true: Check that isEmpty works on an empty deque.")
    public void testIsEmptyTrue() {
        Deque61B<Integer> lld = new LinkedListDeque61B<>();
        assertThat(lld.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("is_empty_false: Check that isEmpty works on a non-empty deque.")
    public void testIsEmptyFalse() {
        Deque61B<Integer> lld = new LinkedListDeque61B<>();
        lld.addLast(1);
        assertThat(lld.isEmpty()).isFalse();
    }

    // Flags for toList tests
    @Test
    @DisplayName("to_list_empty: Check that toList works with empty LinkedListDeque61B.")
    public void testToListEmpty() {
        Deque61B<Integer> lld = new LinkedListDeque61B<>();
        assertThat(lld.toList()).isEmpty();
    }

    @Test
    @DisplayName("to_list_nonempty: Check that toList works with non-empty LinkedListDeque61B.")
    public void testToListNonempty() {
        Deque61B<Integer> lld = new LinkedListDeque61B<>();
        lld.addLast(1);
        lld.addLast(2);
        lld.addLast(3);
        assertThat(lld.toList()).containsExactly(1, 2, 3).inOrder();
    }
}