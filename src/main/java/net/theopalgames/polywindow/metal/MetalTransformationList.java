package net.theopalgames.polywindow.metal;

import net.theopalgames.polywindow.transformation.Transformation;

import java.util.*;

final class MetalTransformationList extends LinkedList<Transformation>  {
    private static final double[][] noTransformations = new double[0][];

    private final long ctx;
    private final MetalNativeCode metal;

    MetalTransformationList(long ctx, MetalNativeCode metal) {
        this.ctx = ctx;
        this.metal = metal;
    }

    @Override
    public boolean add(Transformation transformation) {
        add(size(), transformation);
        return true;
    }

    @Override
    public void add(int index, Transformation element) {
        super.add(index, element);
        refreshTransformations();
    }

    @Override
    public Transformation remove() {
        return remove(0);
    }

    @Override
    public Transformation remove(int index) {
        Transformation transformation = get(index);
        remove(transformation);
        return transformation;
    }

    @Override
    public boolean remove(Object o) {
        boolean res = super.remove(o);
        refreshTransformations();
        return res;
    }

    @Override
    public ListIterator<Transformation> listIterator() {
        return new ListIter(super.listIterator());
    }

    @Override
    public Iterator<Transformation> descendingIterator() {
        return new Iter(super.descendingIterator());
    }

    private class Iter implements Iterator<Transformation> {
        Iterator<Transformation> parent;

        private Iter(Iterator<Transformation> parent) {
            this.parent = parent;
        }

        @Override
        public boolean hasNext() {
            return parent.hasNext();
        }

        @Override
        public Transformation next() {
            return parent.next();
        }

        @Override
        public void remove() {
            parent.remove();
            refreshTransformations();
        }
    }

    private class ListIter extends Iter implements ListIterator<Transformation> {
        private ListIter(ListIterator<Transformation> parent) {
            super(parent);
        }

        private ListIterator<Transformation> parent() {
            return (ListIterator<Transformation>) parent;
        }

        @Override
        public boolean hasPrevious() {
            return parent().hasPrevious();
        }

        @Override
        public Transformation previous() {
            return parent().previous();
        }

        @Override
        public int nextIndex() {
            return parent().nextIndex();
        }

        @Override
        public int previousIndex() {
            return parent().previousIndex();
        }

        @Override
        public void set(Transformation transformation) {
            parent().set(transformation);
            refreshTransformations();
        }

        @Override
        public void add(Transformation transformation) {
            parent().add(transformation);
            refreshTransformations();
        }
    }

    private void refreshTransformations() {
        if (isEmpty()) {
            metal.changeMatrices(ctx, noTransformations);
            return;
        }
    
        List<double[]> transformations = new LinkedList<>();
        this.forEach(transformation -> transformation.addMatrices(transformations::add));
        metal.changeMatrices(ctx, transformations.toArray(noTransformations));
    }
}
