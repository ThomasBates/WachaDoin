package ca.turbobutterfly.android.views;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class PagerAdapter extends FragmentPagerAdapter
{
    private Fragment pages[];

    public PagerAdapter(FragmentManager fm, Fragment... pages)
    {
        super(fm);
        this.pages = pages;
    }

    @Override
    public int getCount()
    {
        return pages.length;
    }

    @Override
    public Fragment getItem(int position)
    {
        return pages[position];
    }

    @Override
    public CharSequence getPageTitle(int position)
    {
        return pages[position].toString();
    }

    @Override
    public int getItemPosition(Object object)
    {
        // Causes adapter to reload all Fragments when
        // notifyDataSetChanged is called
        return POSITION_NONE;
    }
}
