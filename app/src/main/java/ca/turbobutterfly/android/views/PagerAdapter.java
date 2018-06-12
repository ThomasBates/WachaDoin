package ca.turbobutterfly.views;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

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
