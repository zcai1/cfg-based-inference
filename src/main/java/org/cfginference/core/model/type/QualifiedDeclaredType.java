package org.cfginference.core.model.type;

import com.google.auto.value.AutoValue;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import org.cfginference.core.model.qualifier.Qualifier;

import javax.lang.model.type.DeclaredType;

@AutoValue
public abstract class QualifiedDeclaredType<Q extends Qualifier> extends QualifiedType<Q> {
    @Override
    public abstract DeclaredType getJavaType();

    public abstract ImmutableList<QualifiedType<Q>> getTypeArguments();

    public static <Q extends Qualifier> Builder<Q> builder() {
        return new AutoValue_QualifiedDeclaredType.Builder<>();
    }

    public abstract Builder<Q> toBuilder();

    @Override
    public QualifiedDeclaredType<Q> withQualifier(Q qualifier) {
        return toBuilder().setQualifier(qualifier).build();
    }

    @Override
    public final <R, P> R accept(QualifiedTypeVisitor<Q, R, P> v, P p) {
        return v.visitDeclared(this, p);
    }

    @AutoValue.Builder
    public abstract static class Builder<Q extends Qualifier> {

        public abstract Builder<Q> setQualifier(Q qualifier);
        public abstract Builder<Q> setJavaType(DeclaredType type);

        public abstract ImmutableList.Builder<QualifiedType<Q>> typeArgumentsBuilder();

        public final Builder<Q> addTypeArgument(QualifiedType<Q> typeArgument) {
            typeArgumentsBuilder().add(typeArgument);
            return this;
        }

        public final Builder<Q> addTypeArguments(Iterable<QualifiedType<Q>> typeArguments) {
            typeArgumentsBuilder().addAll(typeArguments);
            return this;
        }

        protected abstract QualifiedDeclaredType<Q> autoBuild();

        public final QualifiedDeclaredType<Q> build() {
            QualifiedDeclaredType<Q> type = autoBuild();
            Preconditions.checkState(
                    type.getTypeArguments().size() == type.getJavaType().getTypeArguments().size(),
                    "Type arguments size not matched"
            );
            return type;
        }
    }
}
