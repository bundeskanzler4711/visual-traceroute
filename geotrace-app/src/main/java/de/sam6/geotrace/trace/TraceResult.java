package main.java.de.sam6.geotrace.trace;

import de.sam6.geotrace.geo.Coordinate;

import java.util.Collection;

/**
 * A parsed traceroute line results in a {@see TraceResult} instance. This class contains the output in a schematic,
 * easy accesible form.
 * @author cmichi
 */
public class TraceResult
{
   public Collection<String> pingTime;

   public String hostname;

   public String ip;

   public Coordinate geolocation;

   public int i;

   /**
    * Compute average ping time.
    * @return
    */
   public float getAveragePing()
   {
      float p = 0.0f;
      try
      {
         for(String st : pingTime)
         {
            if(st.trim().length() > 0)
            {
               p += Float.parseFloat(st);
            }
         }
      }
      catch(NumberFormatException e)
      {
         System.out.println("NumberFormatException: " + pingTime);
         p = 1.0f;
      }

      return p / pingTime.size();
   }

}
