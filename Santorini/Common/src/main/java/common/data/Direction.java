package common.data;

import java.util.*;


/**
 * common.data.Direction is an enumeration of positions relative to an origin positions
 * that designate an unambiguous direction. It maps the enum values to
 * the strings used to represent them in the testing harness and network
 * messages
 */
 public class Direction {

   private String eastWest;
   private String northSouth;

   private static final Set<String> LEGAL_EAST_WEST = new HashSet<>(
           Arrays.asList("EAST", "PUT", "WEST")
   );

    private static final Set<String> LEGAL_NORTH_SOUTH = new HashSet<>(
            Arrays.asList("NORTH", "PUT", "SOUTH")
    );

    /**
     * Assumes that due to the assignment criteria that all input will always be valid for direction
     * @param eastWest
     * @param northSouth
     */
   public Direction(String eastWest, String northSouth) {
       String normEastWest = eastWest.toUpperCase();
       String normNorthSouth = northSouth.toUpperCase();

       this.eastWest = normEastWest;
       this.northSouth = normNorthSouth;
   }

   // return an int representing the X vector of this direction
   public int getColumnModifier() {
     switch(this.eastWest) {
         case "EAST":
             return 1;
         case "PUT":
             return 0;
         default:
             return -1;
     }
   }

   // return an int representing the Y vector of this direction
   // NOTE: the coordinate grid uses graphics values, with the origin in the
   // top left, so moving SOUTH increases this value while NORTH decreases
   public int getRowModifier() {
       switch (this.northSouth) {
           case "NORTH":
               return -1;
           case "PUT":
               return 0;
           default:
               return 1;
       }
   }


   // return all possible directions from a position
   public static List<Direction> getAllDirections() {
       List<Direction> result = new ArrayList<>();
       result.add(new Direction("EAST", "NORTH"));
       result.add(new Direction("EAST", "PUT"));
       result.add(new Direction("EAST", "SOUTH"));
       result.add(new Direction("PUT", "NORTH"));
       result.add(new Direction("PUT", "SOUTH"));
       result.add(new Direction("WEST", "NORTH"));
       result.add(new Direction("WEST", "PUT"));
       result.add(new Direction("WEST", "SOUTH"));
       return result;
   }
 }
