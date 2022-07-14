package org.cfginference.core.model.slot;

/**
 * Summary of Shorthand:
 * {@code
 * (@0 | @1) - if (@0 exists) use @0 else use @1
 * (@2 (@0 | @1) - the id of this existential variable is 2, if (@0 exists) use @0 else use @1
 * (@3) - This indicates that an annotation may or may not exist here and its id is @3
 *        IF we have a type parameter <@4 T exntends @5 Object> then
 *            (@3) T is equivalent to <(@3 | @4) T extends (@3 | 5) Object>
 * }
 *
 *
 * ExistentialVariableSlots represent variables that may or may not exist.  These slots
 * represent parametric locations, locations where there is no annotation you could
 * place that would result in an equivalent meaning to omitting the variable.
 *
 * Any non-local use of a type variable is a parametric.  In these cases,
 * the type variable will be given an ExistentialVariableSlot
 *
 * {@code
 * Often in comments, we abbreviate ExistentialVariable slots as either:
 * (@0 | @1) - indicating that if @0 exists then use that otherwise use @1
 * or
 * (@5 (@0 | @1)) - indicating that if @0 exists then use that otherwise use @1
 * and @5 is the identifier for this Existential Variable slot.
 *
 * Finally, if you see a variable alone in parentheses it means that variable may or may
 * not exist:
 * (@2) T  - indicates T may have a primary annotation of @2
 *     If T's declaration were <@0 T extends @1 Object> then
 *         (@2) T corresponds to a type:  <(@2 | @0) T extends (@2 | @1) Object></(@2>
 * }
 *
 * When "normalizing" constraints, we replace ExistentialVariableSlots by translating
 * constraints that contain them into Existential constraints.
 *
 * {@code
 * That is, if we have a constraint:
 *
 * (@0 | @1) <: @3
 *
 * This really states:
 * if (@0 exists) {
 *     @0 <: @3
 * } e;se {
 *     @1 <: @3
 * }
 * }
 */
public class ExistentialSlot extends Slot {
    // a variable whose annotation may or may not exist in source code
    private final Slot potentialSlot;

    // the variable which would take part in a constraint if potentialSlot does not exist
    private final Slot alternativeSlot;

    public ExistentialSlot(Slot potentialSlot, Slot alternativeSlot) {
        // The existential slot shares the same location with the potentialSlot
        super(potentialSlot.getLocation());

        if (alternativeSlot == null) {
            throw new IllegalArgumentException("alternativeSlot cannot be null\n"
                    + "id=" + id + "\n"
                    + "potentialSlot=" + potentialSlot);
        }

        this.potentialSlot = potentialSlot;
        this.alternativeSlot = alternativeSlot;
    }

    public Slot getPotentialSlot() {
        return potentialSlot;
    }

    public Slot getAlternativeSlot() {
        return alternativeSlot;
    }

    @Override
    public boolean isInsertable() {
        return false;
    }

    @Override
    public int hashCode() {
        return 1129 * (potentialSlot.hashCode() + alternativeSlot.hashCode());
    }

    @Override
    public boolean equals(Object thatObj) {
        if (thatObj == this) return true;
        if (!(thatObj instanceof ExistentialSlot)) {
            return false;
        }

        final ExistentialSlot that = (ExistentialSlot) thatObj;
        return this.potentialSlot.equals(that.potentialSlot)
                && this.alternativeSlot.equals(that.alternativeSlot);
    }

    @Override
    public String toString() {
        return "ExistentialVariableSlot(" + getId() + ", (" + potentialSlot.getId() + " | " + alternativeSlot.getId() +")";
    }

    @Override
    public Kind getKind() {
        return Kind.EXISTENTIAL;
    }
}
