import jh61b.utils.Reflection;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.List;

import static com.google.common.truth.Truth.assertThat;
import static com.google.common.truth.Truth.assertWithMessage;

public class ArrayDeque61BTest {

    @Test
    @DisplayName("ArrayDeque61B has no fields besides backing array and primitives")
    void noNonTrivialFields() {
        List<Field> badFields = Reflection.getFields(ArrayDeque61B.class)
                .filter(f -> !(f.getType().isPrimitive() || f.getType().equals(Object[].class) || f.isSynthetic()))
                .toList();

        assertWithMessage("Found fields that are not array or primitives").that(badFields).isEmpty();
    }

    @Test
    @DisplayName("addFirst works on an empty deque")
    void add_first_from_empty() {
        ArrayDeque61B<Integer> deque = new ArrayDeque61B<>();
        deque.addFirst(1);
        assertThat(deque.size()).isEqualTo(1);
        assertThat(deque.get(0)).isEqualTo(1);
    }

    @Test
    @DisplayName("addLast works on an empty deque")
    void add_last_from_empty() {
        ArrayDeque61B<Integer> deque = new ArrayDeque61B<>();
        deque.addLast(1);
        assertThat(deque.size()).isEqualTo(1);
        assertThat(deque.get(0)).isEqualTo(1);
    }

    @Test
    @DisplayName("addFirst works on a non-empty deque")
    void add_first_nonempty() {
        ArrayDeque61B<Integer> deque = new ArrayDeque61B<>();
        deque.addLast(1);
        deque.addFirst(2);
        assertThat(deque.size()).isEqualTo(2);
        assertThat(deque.get(0)).isEqualTo(2);
    }

    @Test
    @DisplayName("addLast works on a non-empty deque")
    void add_last_nonempty() {
        ArrayDeque61B<Integer> deque = new ArrayDeque61B<>();
        deque.addFirst(1);
        deque.addLast(2);
        assertThat(deque.size()).isEqualTo(2);
        assertThat(deque.get(1)).isEqualTo(2);
    }

    @Test
    @DisplayName("addFirst works when called on a full underlying array")
    void add_first_trigger_resize() {
        ArrayDeque61B<Integer> deque = new ArrayDeque61B<>();
        for (int i = 0; i < 8; i++) {
            deque.addLast(i);
        }
        deque.addFirst(8);
        assertThat(deque.size()).isEqualTo(9);
        assertThat(deque.get(0)).isEqualTo(8);
    }

    @Test
    @DisplayName("addLast works when called on a full underlying array")
    void add_last_trigger_resize() {
        ArrayDeque61B<Integer> deque = new ArrayDeque61B<>();
        for (int i = 0; i < 8; i++) {
            deque.addLast(i);
        }
        deque.addLast(8);
        assertThat(deque.size()).isEqualTo(9);
        assertThat(deque.get(8)).isEqualTo(8);
    }

    @Test
    @DisplayName("addFirst works after removing all elements")
    void add_first_after_remove_to_empty() {
        ArrayDeque61B<Integer> deque = new ArrayDeque61B<>();
        deque.addLast(1);
        deque.removeFirst();
        deque.addFirst(2);
        assertThat(deque.size()).isEqualTo(1);
        assertThat(deque.get(0)).isEqualTo(2);
    }

    @Test
    @DisplayName("addLast works after removing all elements")
    void add_last_after_remove_to_empty() {
        ArrayDeque61B<Integer> deque = new ArrayDeque61B<>();
        deque.addFirst(1);
        deque.removeLast();
        deque.addLast(2);
        assertThat(deque.size()).isEqualTo(1);
        assertThat(deque.get(0)).isEqualTo(2);
    }

    @Test
    @DisplayName("removeFirst works")
    void remove_first() {
        ArrayDeque61B<Integer> deque = new ArrayDeque61B<>();
        deque.addLast(1);
        assertThat(deque.removeFirst()).isEqualTo(1);
        assertThat(deque.size()).isEqualTo(0);
    }

    @Test
    @DisplayName("removeLast works")
    void remove_last() {
        ArrayDeque61B<Integer> deque = new ArrayDeque61B<>();
        deque.addFirst(1);
        assertThat(deque.removeLast()).isEqualTo(1);
        assertThat(deque.size()).isEqualTo(0);
    }

    @Test
    @DisplayName("removeFirst works when removing the last element")
    void remove_first_to_empty() {
        ArrayDeque61B<Integer> deque = new ArrayDeque61B<>();
        deque.addLast(1);
        deque.addLast(2);
        deque.removeFirst();
        assertThat(deque.removeFirst()).isEqualTo(2);
        assertThat(deque.size()).isEqualTo(0);
    }

    @Test
    @DisplayName("removeLast works when removing the last element")
    void remove_last_to_empty() {
        ArrayDeque61B<Integer> deque = new ArrayDeque61B<>();
        deque.addFirst(1);
        deque.addFirst(2);
        deque.removeLast();
        assertThat(deque.removeLast()).isEqualTo(2);
        assertThat(deque.size()).isEqualTo(0);
    }

    @Test
    @DisplayName("removeFirst works when removing the second to last element")
    void remove_first_to_one() {
        ArrayDeque61B<Integer> deque = new ArrayDeque61B<>();
        deque.addLast(1);
        deque.addLast(2);
        deque.removeFirst();
        assertThat(deque.removeFirst()).isEqualTo(2);
        assertThat(deque.size()).isEqualTo(0);
    }

    @Test
    @DisplayName("removeLast works when removing the second to last element")
    void remove_last_to_one() {
        ArrayDeque61B<Integer> deque = new ArrayDeque61B<>();
        deque.addFirst(1);
        deque.addFirst(2);
        deque.removeLast();
        assertThat(deque.removeLast()).isEqualTo(2);
        assertThat(deque.size()).isEqualTo(0);
    }

    @Test
    @DisplayName("removeFirst triggers resize when usage factor is <= 25% and array size > 8")
    void remove_first_trigger_resize() {
        ArrayDeque61B<Integer> deque = new ArrayDeque61B<>();
        for (int i = 0; i < 16; i++) {
            deque.addLast(i);
        }
        for (int i = 0; i < 12; i++) {
            deque.removeFirst();
        }
        assertThat(deque.size()).isEqualTo(4);
        assertThat(deque.get(0)).isEqualTo(12);
    }

    @Test
    @DisplayName("removeLast triggers resize when usage factor is <= 25% and array size > 8")
    void remove_last_trigger_resize() {
        ArrayDeque61B<Integer> deque = new ArrayDeque61B<>();
        for (int i = 0; i < 16; i++) {
            deque.addLast(i);
        }
        for (int i = 0; i < 12; i++) {
            deque.removeLast();
        }
        assertThat(deque.size()).isEqualTo(4);
        assertThat(deque.get(0)).isEqualTo(0);
    }

    @Test
    @DisplayName("get works on a valid index")
    void get_valid() {
        ArrayDeque61B<Integer> deque = new ArrayDeque61B<>();
        deque.addLast(1);
        assertThat(deque.get(0)).isEqualTo(1);
    }

    @Test
    @DisplayName("get works on a large, out of bounds index")
    void get_oob_large() {
        ArrayDeque61B<Integer> deque = new ArrayDeque61B<>();
        deque.addLast(1);
        assertThat(deque.get(100)).isNull();
    }

    @Test
    @DisplayName("get works on a negative index")
    void get_oob_neg() {
        ArrayDeque61B<Integer> deque = new ArrayDeque61B<>();
        deque.addLast(1);
        assertThat(deque.get(-1)).isNull();
    }

    @Test
    @DisplayName("size works")
    void size() {
        ArrayDeque61B<Integer> deque = new ArrayDeque61B<>();
        deque.addLast(1);
        assertThat(deque.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("size works after removing all elements")
    void size_after_remove_to_empty() {
        ArrayDeque61B<Integer> deque = new ArrayDeque61B<>();
        deque.addLast(1);
        deque.removeFirst();
        assertThat(deque.size()).isEqualTo(0);
    }

    @Test
    @DisplayName("size works after removing from an empty deque")
    void size_after_remove_from_empty() {
        ArrayDeque61B<Integer> deque = new ArrayDeque61B<>();
        deque.removeFirst();
        assertThat(deque.size()).isEqualTo(0);
    }

    @Test
    @DisplayName("isEmpty works on an empty deque")
    void is_empty_true() {
        ArrayDeque61B<Integer> deque = new ArrayDeque61B<>();
        assertThat(deque.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("isEmpty works on a non-empty deque")
    void is_empty_false() {
        ArrayDeque61B<Integer> deque = new ArrayDeque61B<>();
        deque.addLast(1);
        assertThat(deque.isEmpty()).isFalse();
    }

    @Test
    @DisplayName("toList works with empty ArrayDeque")
    void to_list_empty() {
        ArrayDeque61B<Integer> deque = new ArrayDeque61B<>();
        assertThat(deque.toList()).isEmpty();
    }

    @Test
    @DisplayName("toList works with non-empty ArrayDeque")
    void to_list_nonempty() {
        ArrayDeque61B<Integer> deque = new ArrayDeque61B<>();
        deque.addLast(1);
        assertThat(deque.toList()).containsExactly(1);
    }

    @Test
    @DisplayName("Trigger a resize up and then a resize down in the same test")
    void resize_up_and_resize_down() {
        ArrayDeque61B<Integer> deque = new ArrayDeque61B<>();
        for (int i = 0; i < 16; i++) {
            deque.addLast(i);
        }
        for (int i = 0; i < 12; i++) {
            deque.removeFirst();
        }
        assertThat(deque.size()).isEqualTo(4);
        assertThat(deque.get(0)).isEqualTo(12);
    }
}
