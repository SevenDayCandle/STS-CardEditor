package pinacolada.utilities;

import extendedui.interfaces.delegates.ActionT1;
import extendedui.interfaces.delegates.ActionT3;
import extendedui.interfaces.delegates.FuncT3;

import java.util.ArrayList;
import java.util.List;

public abstract class ListSelection<Item>
{
    public enum Mode
    {
        Random,
        Last,
        First,
        Special;

        //@Formatter: Off
        public boolean isFirst()  { return First.equals(this);  }
        public boolean isRandom()  { return Random.equals(this);  }
        public boolean isLast()     { return Last.equals(this);     }
        public boolean isSpecial() { return Special.equals(this); }
        //@Formatter: On
    }

    public static final ListSelection<Object> Last = last(0);
    public static final ListSelection<Object> First = first(0);
    public static final ListSelection<Object> Random = random(null);
    public static final ListSelection<Object> Default = Last;

    public final Mode mode;

    public abstract void add(List<Item> list, Item item, int index);
    public abstract Item get(List<Item> list, int index, boolean remove);

    protected ListSelection(Mode mode)
    {
        this.mode = mode;
    }

    public static <T> ListSelection<T> last(int shift)
    {
        return new Last<>(shift);
    }

    public static <T> ListSelection<T> first(int shift)
    {
        return new First<>(shift);
    }

    public static <T> ListSelection<T> random(com.megacrit.cardcrawl.random.Random rng)
    {
        return new Random<>(rng);
    }

    public static <T> ListSelection<T> special(ActionT3<List<T>, T, Integer> add,
                                               FuncT3<T, List<T>, Integer, Boolean> get)
    {
        return new Special<>(add, get);
    }

    public void forEach(ArrayList<Item> modifiableList, int amount, ActionT1<Item> apply)
    {
        final boolean remove = mode.isRandom();
        final int max = Math.min(modifiableList.size(), amount);
        for (int i = 0; i < max; i++)
        {
            Item card = get(modifiableList, i, remove);
            if (card != null)
            {
                apply.invoke(card);
            }
        }
    }

    private static class First<T> extends ListSelection<T>
    {
        private final int shift;

        public First(int shift)
        {
            super(Mode.First);

            this.shift = shift;
        }

        @Override
        public void add(List<T> list, T item, int index)
        {
            list.add(Math.max(0, Math.min(list.size(), index + shift)), item);
        }

        @Override
        public T get(List<T> list, int index, boolean remove)
        {
            T card = null;
            int position = index + shift;
            if (position >= 0 && position < list.size())
            {
                card = list.get(position);
                if (remove)
                {
                    list.remove(position);
                }
            }

            return card;
        }
    }

    private static class Last<T> extends ListSelection<T>
    {
        private final int shift;

        public Last(int shift)
        {
            super(Mode.Last);

            this.shift = shift;
        }

        @Override
        public void add(List<T> list, T item, int index)
        {
            list.add(Math.max(0, Math.min(list.size(), list.size() - index - shift)), item);
        }

        @Override
        public T get(List<T> list, int index, boolean remove)
        {
            T card = null;
            int position = list.size() - 1 - index - shift;
            if (position >= 0 && position < list.size())
            {
                card = list.get(position);
                if (remove)
                {
                    list.remove(position);
                }
            }

            return card;
        }
    }

    private static class Random<T> extends ListSelection<T>
    {
        private final com.megacrit.cardcrawl.random.Random rng;

        private Random(com.megacrit.cardcrawl.random.Random rng)
        {
            super(Mode.Random);

            this.rng = rng;
        }

        protected int getRandomIndex(int size)
        {
            if (size <= 0)
            {
                return 0;
            }
            else if (rng == null)
            {
                return GameUtilities.getRNG().random(size);
            }
            else
            {
                return rng.random(size);
            }
        }

        @Override
        public void add(List<T> list, T item, int index)
        {
            list.add(getRandomIndex(list.size() - 1), item);
        }

        @Override
        public T get(List<T> list, int index, boolean remove)
        {
            T card = null;
            if (list.size() > 0)
            {
                int position = getRandomIndex(list.size() - 1);
                card = list.get(position);
                if (remove)
                {
                    list.remove(position);
                }
            }

            return card;
        }
    }

    private static class Special<T> extends ListSelection<T>
    {
        private ActionT3<List<T>, T, Integer> addCard;
        private FuncT3<T, List<T>, Integer, Boolean> getCard;

        public Special(ActionT3<List<T>, T, Integer> add,
                FuncT3<T, List<T>, Integer, Boolean> get)
        {
            super(Mode.Special);

            this.addCard = add;
            this.getCard = get;
        }

        @Override
        public void add(List<T> list, T item, int index)
        {
            addCard.invoke(list, item, index);
        }

        @Override
        public T get(List<T> list, int index, boolean remove)
        {
            return getCard.invoke(list, index, remove);
        }
    }
}
