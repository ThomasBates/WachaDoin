package ca.turbobutterfly.core.mvvm;

import java.lang.reflect.Type;
import java.util.HashMap;

class ValidationContext implements IValidationContext
{
    public ValidationContext(Object instance, HashMap<Object, Object> items, Object provider)
    {

    }

    @Override
    public String DisplayName()
    {
        return null;
    }

    @Override
    public void DisplayName(String displayName)
    {

    }

    @Override
    public HashMap<Object, Object> Items()
    {
        return null;
    }

    @Override
    public String MemberName()
    {
        return null;
    }

    @Override
    public void MemberName(String memberName)
    {

    }

    @Override
    public Object ObjectInstance()
    {
        return null;
    }

    @Override
    public Type ObjectType()
    {
        return null;
    }
}
