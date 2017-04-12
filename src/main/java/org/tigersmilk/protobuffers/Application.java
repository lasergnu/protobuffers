package org.tigersmilk.protobuffers;

import com.google.protobuf.ExtensionRegistry;

import static org.tigersmilk.QueryProtos.fooDog;
import static org.tigersmilk.QueryProtos.fooCat;
import static org.tigersmilk.QueryProtos.registerAllExtensions;

// Imports used for Mechas and some we didn't
import org.tigersmilk.QueryProtos.TheBeast;
import org.tigersmilk.QueryProtos.Zaku;
import org.tigersmilk.QueryProtos.MechaMessage;
import org.tigersmilk.QueryProtos.MechaMessage.MechaCase;

// Imports for Pets
import org.tigersmilk.QueryProtos.PetMessage;
import org.tigersmilk.QueryProtos.Cat;
import org.tigersmilk.QueryProtos.Dog;

// Imports used for Person
import org.tigersmilk.QueryProtos.Person;
import org.tigersmilk.QueryProtos.Person.PhoneNumber;

public class Application
{
	public static void main( String[] args ) throws Exception
	{
    System.out.println("This is the Google example:");

    Person john =
      Person.newBuilder()
      .setId(1234)
      .setName("John Doe")
      .setEmail("jdoe@example.com")
      .addPhones(
                Person.PhoneNumber.newBuilder()
                .setNumber("555-4321")
                .setType(Person.PhoneType.HOME).build())
      .build();

    System.out.println("This is the person: " + john.toString());

    System.out.println("Now try the OneOf Case: Gundam, Evangelion");

    Zaku zaku = Zaku.newBuilder()
      .setType("Rust bucket")
      .build();

    MechaMessage mmsg = MechaMessage.newBuilder()
      .setZakuValue(zaku)
      .build();

    byte[] mmsgBytes = mmsg.toByteArray();
    // Send Zaku to internets
    MechaMessage dlMsg = MechaMessage.parseFrom(mmsgBytes);

    // Play with message to show off the built in enums
    switch (dlMsg.getMechaCase()) {
    case ZAKU_VALUE:
      System.out.println("We can ask for Zaku");
      System.out.println("Zaku is " + dlMsg.getZakuValue());
      break;
    }

    System.out.println("Final test using extensions with pets.");
    // Need this or you cannot deserialize.
    ExtensionRegistry registry = ExtensionRegistry.newInstance();
    org.tigersmilk.QueryProtos.registerAllExtensions(registry);

    // Unlike Oneof - we can have more than one extension
    // So this is not really subclassing or C-union like.

    Dog dog = Dog.newBuilder()
      .setName("Mudge")
      .setBarkVolume(10)
      .build();

    Cat cat = Cat.newBuilder()
      .setName("Zach")
      .setPurrWavelength(50)
      .build();

    PetMessage pMsg = PetMessage.newBuilder()
      .setMessageType("Dog")
      .setExtension(fooDog, dog)
      .setExtension(fooCat, cat)
      .build();
    byte[] pmsgBytes = pMsg.toByteArray();
    // Cats in tubes and Dogs: much fur, so bow wow...
    PetMessage dlPet = PetMessage.parseFrom(pmsgBytes, registry);
    System.out.println("Do we have the fooDog extension? " +
                       dlPet.hasExtension(fooDog));
    System.out.println("Do we have the fooCat extension? " +
                       dlPet.hasExtension(fooCat));

    System.out.println("The Dog is " +
                       dlPet.getExtension(fooDog));
  }
}
