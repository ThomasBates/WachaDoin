package ca.turbobutterfly.core.mvvm;

import java.lang.reflect.Type;
import java.util.HashMap;

public interface IValidationContext
{
    String DisplayName();
    void DisplayName(String displayName);

    HashMap<Object, Object> Items();

    String MemberName();
    void MemberName(String memberName);

    Object ObjectInstance();
    Type ObjectType();
}
