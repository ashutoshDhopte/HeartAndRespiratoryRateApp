# Context Monitoring Application

**Q1)** Imagine you are new to the programming world and not proficient enough in coding. But, you have a brilliant idea where you want to develop a context-sensing application like Project 1.  You come across the Heath-Dev paper and want it to build your application. Specify what Specifications you should provide to the Health-Dev framework to develop the code ideally.

**Ans.** 

          a. Hardware
                    i. Android phone with a camera, flashlight, an accelerometer, and Android 14.
                    ii. Macbook with Apple silicon.
          b. Software
                    i. Android studio to build the app.
                    ii. Data structure of the resulting database, including the name and type of the columns.
          c. Libraries
                    i. Jetpack Compose - Kotlin
                    ii. CameraX
                    iii. Room database
          d. Algorithm
                    i. To use the intensity of red color in a video to measure the heart rate.
                    ii. To calculate the respiratory rate using the 3-dimensional values from the accelerometer.
                    
         Algorithm -- Respiratory Rate Calculation:
         
          1: Input: 𝑎𝑐𝑐𝑒𝑙𝑉𝑎𝑙𝑢𝑒𝑠𝑋, 𝑎𝑐𝑐𝑒𝑙𝑉𝑎𝑙𝑢𝑒𝑠𝑌, 𝑎𝑐𝑐𝑒𝑙𝑉𝑎𝑙𝑢𝑒𝑠𝑍
          2: Output: Respiratory Rate (Integer)
          3: 𝑝𝑟𝑒𝑣𝑖𝑜𝑢𝑠𝑉𝑎𝑙𝑢𝑒 ← 10
          4: 𝑐𝑢𝑟𝑟𝑒𝑛𝑡𝑉𝑎𝑙𝑢𝑒 ← 0
          5: 𝑘 ← 0
          6: for 𝑖 ∈ [11, 𝑎𝑐𝑐𝑒𝑙𝑉𝑎𝑙𝑢𝑒𝑠𝑌 .𝑠𝑖𝑧𝑒] do
          7: 𝑐𝑢𝑟𝑟𝑒𝑛𝑡𝑉𝑎𝑙𝑢𝑒 ←√︁ 𝑎𝑐𝑐𝑒𝑙𝑉𝑎𝑙𝑢𝑒𝑠𝑋 [𝑖]2 + 𝑎𝑐𝑐𝑒𝑙𝑉𝑎𝑙𝑢𝑒𝑠𝑌 [𝑖]2 + 𝑎𝑐𝑐𝑒𝑙𝑉𝑎𝑙𝑢𝑒𝑠𝑍 [𝑖]2
          8: if | 𝑝𝑟𝑒𝑣𝑖𝑜𝑢𝑠𝑉𝑎𝑙𝑢𝑒 − 𝑐𝑢𝑟𝑟𝑒𝑛𝑡𝑉𝑎𝑙𝑢𝑒| > 0.15 then
          9: 𝑘 ← 𝑘 + 1
          10: end if
          11: 𝑝𝑟𝑒𝑣𝑖𝑜𝑢𝑠𝑉𝑎𝑙𝑢𝑒 ← 𝑐𝑢𝑟𝑟𝑒𝑛𝑡𝑉𝑎𝑙𝑢𝑒
          12: end for
          13: 𝑟𝑒𝑡 ← [𝑘 / 45]
          14: return ⌊𝑟𝑒𝑡 × 30⌋
          
          Table structure:
          
          monitor(
                 monitor_id int primary_key auto_increment,
                 heart_rate int,
                 respiratory_rate int,
                 symptom_rate_nausea int,
                 symptom_rate_headache int,
                 symptom_rate_diarrhea int,
                 symptom_rate_soar_throat int,
                 symptom_rate_fever int,
                 symptom_rate_muscle_ache int,
                 symptom_rate_loss_of_smell_or_taste int,
                 symptom_rate_cough int,
                 symptom_rate_shortness_of_breath int,
                 symptom_rate_feeling_tired int,
                 created_on long
          )

**Q2)** In Project 1 you have stored the user’s symptoms data in the local server. Using the bHealthy application suite how can you provide feedback to the user and develop a novel application to improve context sensing and use that to generate the model of the user?

**Ans.**  

          a. Using bHealthy I can collect additional user data such as ECG, EEG, distance traveled, heart rate, etc.
          
          b. These data can be summarized daily, weekly, monthly, and yearly. 
          
          c. We can calculate the trend of the user's frequency of working out and other activities. This trend can be shown in a graph to make users understand easily.
          
          d. For each activity and spike in the heart rate and distance traveled users will be awarded certain points. So they can set goals for each day.
          
          e. We can indicate the number of calories burnt daily on the calendar by drawing a circle over the numbers, with the area of the circle corresponding to the calories.
          
          f. Internally we can set some values as the threshold for the heart rate, ECG, EEG, etc. So while collecting the data, we can inform them whether they have some medical condition or suggest visiting a doctor. These values have to be reviewed by a doctor.
          
          g. We should upload the data to the cloud to support viewing it on multiple devices.
          
          h. After collecting the user's symptoms for a week or a month, we can show a trend for each symptom and highlight any abnormalities.

**Q3)** A common assumption is mobile computing is mostly about app development. After completing Project 1 and reading both papers, have your views changed? If yes, what do you think mobile computing is about and why? If no, please explain why you still think mobile computing is mostly about app development, providing examples to support your viewpoint.

**Ans.** 

          As a developer with 3.5 years of experience in more than one industry, I know that developing a system is not mainly about coding, but to listening and identifying client's problems and solving them efficiently. 
          My views were the same from the start, that mobile computing is not about app development, but app development is just a part of it. The main purpose and the reasoning behind mobile computing is to identifying the problems and solving them using mobile hardwares, software and network. Because no matter how good you code, breakthrough happens with an idea. Mobile computing focuses on how we perceive real-world and interact with it remotely. Just as we developed project 1, using the sensors and hardwares we figured out to measure something which requires more advance tech. This innovation is what mobile computing is about. Coding can be done is any language, using any libraries and software versions, but this idea is the foundation.
