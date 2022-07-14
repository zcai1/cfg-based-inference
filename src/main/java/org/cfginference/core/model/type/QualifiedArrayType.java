package org.cfginference.core.model.type;

import com.google.auto.value.AutoValue;
import org.cfginference.core.model.qualifier.Qualifier;

import javax.lang.model.type.ArrayType;

@AutoValue
public abstract class QualifiedArrayType<Q extends Qualifier> extends QualifiedType<Q> {
    @Override
    public abstract ArrayType getJavaType();

    public abstract QualifiedType<Q> getComponentType();

    public static <Q extends Qualifier> Builder<Q> builder() {
        return new AutoValue_QualifiedArrayType.Builder<>();
    }

    public abstract Builder<Q> toBuilder();

    @Override
    public QualifiedArrayType<Q> withQualifier(Q qualifier) {
        return toBuilder().setQualifier(qualifier).build();
    }

    @Override
    public final <R, P> R accept(QualifiedTypeVisitor<Q, R, P> v, P p) {
        return v.visitArray(this, p);
    }

    @AutoValue.Builder
    public abstract static class Builder<Q extends Qualifier> {
        public abstract Builder<Q> setQualifier(Q qualifier);
        public abstract Builder<Q> setJavaType(ArrayType type);
        public abstract Builder<Q> setComponentType(QualifiedType<Q> componentType);
        public abstract QualifiedArrayType<Q> build();
    }
}
